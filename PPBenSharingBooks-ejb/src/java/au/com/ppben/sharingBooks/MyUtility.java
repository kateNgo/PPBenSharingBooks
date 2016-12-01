/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.ppben.sharingBooks;

import au.com.ppben.sharingBooks.domain.AccountType;
import java.security.*;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import java.util.LinkedHashMap;
import java.util.Base64;
import java.util.Map;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;
/**
 *
 * @author phuong
 */
public class MyUtility {
    /**
     * These static variables are to use Twilio web service for sending SMS.
     * These belong to Aiden's trial account
     */
    public static final String ACCOUNT_SID = "AC7213f5dd76d812d0ddda45827a727cf4";
    public static final String AUTH_TOKEN = "2d5a93cab623538c1640f6c6b5696301";
    
    /**
     * These static variables are to use sending email by Gmail.
     */
    private static final String USERNAME = "aip.sharingbook@gmail.com";  // GMail user name 
    private static final String PASSWORD = "utsstudent"; // GMail password
    private static final String HOST = "smtp.gmail.com";
    private static final int PORT = 465;
    private static final String FROM = "aip.sharingbook@gmail.com";
    private static final boolean AUTH = true;
    private static final boolean DEBUG = false;
    
    // Base URL of validation link
    private static final String VALIDATION_URL = "http://localhost:8080/SharingBook-war/faces/activate.xhtml?key=";
    // Login URL for mail of reset password
    private static final String LOGIN_URL = "http://localhost:8080/SharingBook-war/faces/login.xhtml";
    
        
    /**
     * This method is to check a string is null and empty string
     * @param str
     * @return true if string is null or blank
     */
    public static boolean isEmptyString(String str) {
        return ((str == null) || (str.isEmpty()));
    }

    /**
     * This method is to encode string with SHA-256
     * @param data
     * @return string after encode
     * @throws NoSuchAlgorithmException 
     */
    public static String hash256(String data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(data.getBytes());
        return bytesToHex(md.digest());
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte byt : bytes) {
            result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }
    /**
     * Adds a message to the current faces context, so that it will appear in
     * a h:messages element.
     * @param message the text of the error message to show the user
     */
    public static void showError(String message) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(message));
    }
    
    /**
     * Method to send sms via Twilio web service.
     * @param receiveNo as mobile number of receiver
     * @param msg as message of sms
     */
    public static void smsSender(String receiveNo, String msg) {
        String target = "https://api.twilio.com/2010-04-01";
        String path = "Accounts/" + ACCOUNT_SID + "/Messages.json";
        
        Client client = ClientBuilder.newBuilder().build();
        WebTarget myResource = client.target(target);
        
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("Body", msg);
        params.put("To", receiveNo);
        //From number is set by registered number on Twilio
        params.put("From", "+61476856603");
        
        /* Set header for HTTP authentication by following Twilio security rule
        * reference : https://www.twilio.com/docs/api/security
        * Using Basic access authentication 
        */
        Response response = myResource
                .path(path)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString((ACCOUNT_SID + ":" + AUTH_TOKEN).getBytes(StandardCharsets.UTF_8)))
                .method(HttpMethod.POST, Entity.form(new MultivaluedHashMap<>(params)));
         
         //Exception process by response status
         try {
            if (response.getStatus() != 201) {
                if (Response.Status.Family.familyOf(response.getStatus()) == Response.Status.Family.CLIENT_ERROR) {
                    showError("Responese Error - Response Code: " + response.getStatus());
                } else {
                    showError("Unable to send Twilio SMS: " + response.getStatus() + "\nSMS: " + params);
                }
            }
        } finally {
            response.close();
        }
        client.close();
    }
    
    /**
     * Method to send email by java API.
     * Reference: http://www.oracle.com/webfolder/technetwork/tutorials/obe/java/javamail/javamail.html 
     * @param to email address of recipient
     * @param subject email subject
     * @param body email body text/html
     * @throws MessagingException 
     */
     public static void sendEmail(String to, String subject, String body) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.ssl.enable", true);
        
        Authenticator authenticator = null;
        if (AUTH) {
            props.put("mail.smtp.auth", true);
            authenticator = new Authenticator() {
                private PasswordAuthentication pa = new PasswordAuthentication(USERNAME, PASSWORD);
                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    return pa;
                }
            };
        }        
        MimeBodyPart mbp = new MimeBodyPart(); 
        mbp.setContent(body, "text/html"); 
        MimeMultipart multipart = new MimeMultipart(); 
        multipart.addBodyPart(mbp);
        
        Session session = Session.getInstance(props, authenticator);
        session.setDebug(DEBUG);
        
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(FROM));
            message.setContent(multipart);
            InternetAddress address = new InternetAddress(to);
            message.setRecipient(RecipientType.TO, address);
            message.setSubject(subject);
            message.setText(body, "UTF-8", "html");
            Transport.send(message);
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }
     
    /**
     * This method was implemented by my class mate
     * This method to generate body message of validation email with validation link generator.
     * @param keyBase as base of key generation
     * @return message as body of validation email
     */ 
    public static String validationMessage (String keyBase) {
        String message;
        String key=null;
        String link;        
        
        try {
            key = keyBase + "+" + hash256(keyBase);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(MyUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        link = "<a href=\"" + VALIDATION_URL + key + "\"> Click here</a>";
        message = "To validation your account" + link;
        
        return message;
    }
    
    /**
     * This method to generate body message of reset password email with new password.
     * @param password as new password
     * @return message as body of reset password email
     */
    public static String resetPwMessage (String password) {
        String message;
        String link;
        
        link = "<a href=\"" + LOGIN_URL + "\"> Click here</a>";
        message = "Your password has been reset by <strong>" + password + "</strong>.<br><br>";
        message += "Pleas, chage your password after log in. <br>";
        message += "To log in SharingBook please " + link;        
        
        return message;
    }
    
    /**
     * Method to generate random String by UUID
     * @param length as length of random string
     * @return  generated random string
     */
    public static String randomString(int length) {
        return UUID.randomUUID().toString().substring(0, length);
    }
    public static void main(String[] args){
        System.out.println(AccountType.ADMIN.name());
        System.out.println(AccountType.ADMIN.toString());
    }
}
