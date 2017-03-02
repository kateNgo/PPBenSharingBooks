/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.ppben.sharingBooks.domain;

/**
 *
 * @author phuong
 */
public enum AccountType {

    ADMIN("Administrator user"),
    USER("User user"),
    GUEST("Guest");
    private final String label;

    private AccountType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
