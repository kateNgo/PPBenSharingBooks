/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.ppben.sharingBooks.bean;

/**
 *
 * @author phuong
 */

import au.com.ppben.sharingBooks.MyUtility;
import au.com.ppben.sharingBooks.domain.Account;
import au.com.ppben.sharingBooks.remote.AccountBeanRemote;
import java.io.Serializable;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class AccountBean implements Serializable, AccountBeanRemote {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Account find(String email) {
        return em.find(Account.class, email);
    }

    @Override
    public List<Account> findAll() {
        TypedQuery<Account> query = em.createNamedQuery("loadAllAccounts", Account.class);
        return query.getResultList();
    }

    @Override
    public List<Account> searchAccounts(String name) {
        TypedQuery<Account> query = em.createNamedQuery("searchAccounts", Account.class);
        if (!MyUtility.isEmptyString(name)) {
            query.setParameter("name", "%" + name.toLowerCase() + "%");
        } else {
            query.setParameter("name", "%");
        }
        return query.getResultList();
    }

    @Override
    public Account addNewAccount(Account account) {
        em.persist(account);
        em.flush();
        return account;
    }

    @Override
    public void deleteAccount(String email) {
        Account c = em.find(Account.class, email);
        em.remove(c);
        em.flush();
    }

    @Override
    public Account updateAccount(Account account) {
        em.merge(account);
        em.flush();
        return account;
    }
}
