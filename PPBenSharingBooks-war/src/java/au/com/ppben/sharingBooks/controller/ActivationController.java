/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.ppben.sharingBooks.controller;

import au.com.ppben.sharingBooks.MyUtility;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import au.com.ppben.sharingBooks.domain.Account;
import au.com.ppben.sharingBooks.domain.AccountType;
import au.com.ppben.sharingBooks.remote.AccountBeanRemote;
import javax.ejb.EJB;
/**
 *
 * @author Aiden Seonghwa Son
 * @Author Kate Ngo
 */
@RequestScoped
@ManagedBean
public class ActivationController implements Serializable {
    
    @ManagedProperty(value="#{param.key}")
    private String key;
        
    @EJB
    private AccountBeanRemote accountBean;
    private Account account = new Account();
    private boolean valid;
    
    @PostConstruct
    public void init() {
        try {
            valid = keyValidator(key);
        } catch (Exception e) {
            System.out.println("Exception = " + e);
        }
    }

    /**
     * Method to get valid
     * @return valid as boolean
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Method to get key
     * @return key
     */
    public String getKey() {
        return key;
    }

    /**
     * Method to set key
     * @param key 
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Method to validate parameter key
     * @param key as received key form user validation email
     * @return valid as true or false
     */
    public boolean keyValidator(String key) {
        if (key != null) {
            String[] keySplit = key.split(" ");
            System.out.println("user ID = " + keySplit[0]);
            System.out.println("user key = " + keySplit[1]);
            account = accountBean.find(keySplit[0]);
            String email = account.getEmail();
            System.out.println("Account role: " + account.getAccountType());
            if (account.getAccountType().equals(AccountType.GUEST.name())) {
                String matchingKey;
                try {
                    matchingKey = MyUtility.hash256(email);
                    if (matchingKey.equals(keySplit[1])) {
                        activateAccount(account);
                        valid = true;
                    } else {
                        valid = false;
                    }
                } catch (Exception e) {
                    System.out.println("Matching key exception = " + e);
                }
            } else {
                valid = true;
            }            
        }
        return valid;
    }
    
    /**
     * Method to update user Role from GUEST to USER
     * @param userAccount as Account class object
     */ 
    public void activateAccount(Account userAccount) {
        try {
            //Update role table that role column from GUEST to USER
            userAccount.setAccountType(AccountType.USER.toString());
            accountBean.updateAccount(userAccount);
            System.out.println("Account's role is updated : " + userAccount.getAccountType() );
        } catch (Exception e) {
            System.out.println("Activate account exception = " + e);
        }
    }
}
