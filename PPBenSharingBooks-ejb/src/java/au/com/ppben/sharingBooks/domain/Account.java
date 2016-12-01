/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.ppben.sharingBooks.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 *
 * @author phuong ngo
 * This represents for a row in the member table
 */
@Entity
@NamedQueries({
    @NamedQuery(
            name = "loadAllAccounts",
            query = "select a from Account a"),
     @NamedQuery(
            name = "searchAccounts",
            query = "select c from Account c where lower(c.fullName) like :name")
})
public class Account implements Serializable{
    
    @Id
    @Column
    @Pattern(regexp = "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)", message = "Invalid email address")
    private String email;
    
    @Column
    @NotNull
    private String password;
    @Column
    private String fullName;
    @Column
    private String accountType;
    //Twilio takes E.164 format for mobile number
    @Column
    //@Pattern(regexp = "^\\+(?:[0-9] ?){6,14}[0-9]$", message = "Invalid mobile, it sholud be E.164 format. ex) +61402000000")
    private String mobile;

    @OneToMany(mappedBy = "account",orphanRemoval=true, cascade={CascadeType.ALL})
    List<Book> myBooks;
    
    /**
     * This is default constructor
     */
    public Account() {
        this.myBooks = new ArrayList();
    }    
   
    /**
     * This is to get the value of password of member
     * @return String that is value of password. 
     */
    public String getPassword() {
        return password;
    }
/**
     * this is to set the value of password
     * @param password. The value is not allowed empty 
     */
    public void setPassword(String password) {
        this.password = password;
    }
    /**
     * This is to return the value of full name 
     * @return String that is value of full name
     */
    public String getFullName() {
        return fullName;
    }
    /**
     * this is to set the value of full name
     * @param fullName. 
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * This is to return the value of email address
     * @return String that is value of email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * this is to set the value of email address
     * @param email. 
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * This is to return the value of mobile number 
     * @return String that is value of mobile number
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * this is to set the value of mobile number
     * @param mobile. 
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    
    

    public List<Book> getMyBooks() {
        return myBooks;
    }

    public void setMyBooks(List<Book> myBooks) {
        this.myBooks = myBooks;
    }

    /**
     * This is account type attribute
     * @return accountType the type of account: user or administrator
     */
    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
    
    public boolean isAdmin(){
        if (this.accountType.equals(AccountType.ADMIN.toString()))
            return true;
        return false;
    }
   
    
   
    
}
