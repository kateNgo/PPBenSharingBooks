/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.ppben.sharingBooks.remote;

import au.com.ppben.sharingBooks.domain.Account;
import java.util.List;

/**
 *
 * @author phuong
 */
public interface AccountBeanRemote {

    /**
     * This method is to get member by user name value
     *
     * @param email
     * @return Member that is found or null if not found
     *
     */
    public Account find(String email);

    /**
     * This method is to get all members in the member table
     *
     * @return List list of members
     *
     */
    public List<Account> findAll();

    /**
     * This method is to get all members in the member table
     *
     * @param name
     * @return List list of members
     *
     */
    public List<Account> searchAccounts(String name);

    /**
     * This method is to create a new account
     *
     * @param account
     * @return account the new account added
     */
    public Account addNewAccount(Account account);

    /**
     * This method is to delete an account
     *
     * @param email the user name's account that will be deleted
     */
    public void deleteAccount(String email);

    /**
     * This method is to update an account
     *
     * @param account the updated account
     * @return account the updated account
     */
    public Account updateAccount(Account account);

}
