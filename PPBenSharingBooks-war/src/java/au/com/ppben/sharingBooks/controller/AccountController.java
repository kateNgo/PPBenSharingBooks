package au.com.ppben.sharingBooks.controller;

import au.com.ppben.sharingBooks.MyUtility;
import au.com.ppben.sharingBooks.domain.Account;
import au.com.ppben.sharingBooks.domain.AccountType;
import au.com.ppben.sharingBooks.remote.AccountBeanRemote;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.jboss.logging.Logger;

/**
 *
 * @author phuong ngo * This is the controller for account . It has session
 * scope
 */
@Named
@SessionScoped
public class AccountController implements Serializable {

    @EJB
    private AccountBeanRemote accountBean;

    private static final String USERNAME_AVAILABLE = "User name is available";
    private static final String USERNAME_EXIST = "This user name existed";
    private static final String RETYPING_PASSWORD_DIFFERENT = "Retyping password is different";
    private static final String PASSWORD_ERROR = "Password is wrong";
    private static final String CHANGE_PASSWORD_SUCCESS = "Password changed successfully.";
    Logger log = Logger.getLogger(this.getClass().getName());

    private Account account = new Account();
    private String email;
    private String oldPassword;
    private String newPassword;
    private String password;

    private String searchTerm;

    private String reTypingPassword;
    private boolean userAvailable = false;

    // Length of generated random password
    private static final int LENGTH_PW = 6;

    @PostConstruct
    public void init() {
        account = new Account();
    }

    public Account getAccount() {

        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    /**
     * This attribute is to check user reType password is the same as the first
     * typing or not
     *
     * @return string
     */
    public String getReTypingPassword() {
        return reTypingPassword;
    }

    public void setReTypingPassword(String reTypingPassword) {
        this.reTypingPassword = reTypingPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * This property is for search term in user list page
     *
     * @return string the value of search term
     */
    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    /**
     * the method is called when user sign up
     *
     * @return String of confirmRegister page or register form with errors
     * @throws javax.mail.MessagingException
     */
    public String register() throws MessagingException {

        log.info("Creating a new member");
        try {
            if (!userAvailable) {
                MyUtility.showError(USERNAME_EXIST);
                return "createAccount";
            }
            if (!checkRetypingPassword()) {
                MyUtility.showError(RETYPING_PASSWORD_DIFFERENT);
                return "createAccount";
            }
            account.setPassword(MyUtility.hash256(account.getPassword()));
            account.setAccountType(AccountType.GUEST.toString());
            account = accountBean.addNewAccount(account);
            //Send validation email with validation URL
            String msg = MyUtility.validationMessage(account.getEmail());
            MyUtility.sendEmail(account.getEmail(), "SharingBook account validation email", msg);
            account = new Account();
        } catch (NoSuchAlgorithmException ee) {
            MyUtility.showError(ee.getMessage());
            return "";
        }

        return "/confirmRegister?faces-redirect=true";
    }

    /**
     * this method is to call when user login the system
     *
     * @return string the page that login successfully
     * @throws java.lang.Exception
     */
    public String login() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            request.logout();
        } catch (ServletException ex) {
            java.util.logging.Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            request.login(email, password);
            account = accountBean.find(email);
            return "/secure/searchBooks?faces-redirect=true";

        } catch (ServletException e) {
            MyUtility.showError("invalid username or password.");
            MyUtility.showError(e.getMessage());

            if (e.getMessage().equalsIgnoreCase("login failed")) {
                account = new Account();
            }
            return null;
        }
    }

    /**
     * This method is to check the user typing password in the second time
     *
     * @return String null if retyping is the same, otherwise is error
     */
    public boolean checkRetypingPassword() {

        return account.getPassword().equals(reTypingPassword);
    }

    /**
     * This method is to check the user name exists or not. It is called by ajax
     * with event onChange of user name input
     *
     * @return string the user name is available or not
     */
    public String getUserAvailable() {

        if (MyUtility.isEmptyString(account.getEmail())) {
            userAvailable = false;
            return "";
        }
        if (accountBean.find(account.getEmail()) == null) {
            userAvailable = true;
            return USERNAME_AVAILABLE;
        } else {
            userAvailable = false;
            return USERNAME_EXIST;
        }

    }

    /**
     * This method is called when the system logout
     */
    public void resetAccount() {
        System.out.println("resetAccount called");
        account = new Account();
    }

    /**
     * Logs out the current user of the container-managed authentication.
     *
     * @throws ServletException if there is no currently logged in user
     */
    public void logout() throws ServletException {
        System.out.println("logout called");
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        request.logout();

        resetAccount();
    }

    /**
     * the method is to list users by name
     *
     * @return list the list of accounts
     */
    public List<Account> searchUsers() {
        return accountBean.searchAccounts(searchTerm);
    }

    /**
     * this method is called when the user wants to edit the user owns
     *
     * @param email
     * @return String
     */
    public String editUser(String email) {
        account = accountBean.find(email);
        return "/admin/editUser?faces-redirect=true";
    }

    /**
     * this method is to delete an account with username
     *
     * @param email the user name will be deleted
     */
    public void deleteUser(String email) {

        accountBean.deleteAccount(email);
    }

    public String updateUser() {

        account = accountBean.updateAccount(account);
        return "/secure/searchUsers?faces-redirect=true";
    }

    /**
     * used to update account info by user
     *
     * @return
     */
    public String updateAccount() {
        /*
        if (!checkRetypingPassword()) {
            MyUtility.showError(RETYPING_PASSWORD_DIFFERENT);
            return null;
        }
         */
        try {
            account.setPassword(MyUtility.hash256(account.getPassword()));
        } catch (NoSuchAlgorithmException ex) {
            java.util.logging.Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE, null, ex);
        }
        account = accountBean.updateAccount(account);
        return "/secure/searchBooks?faces-redirect=true";
    }

    /**
     * used to update account info by user
     *
     * @return
     */
    public String changePassword() {

        if (!this.newPassword.equals(this.reTypingPassword)) {
            MyUtility.showError(RETYPING_PASSWORD_DIFFERENT);
            return null;
        }
        try {

            if (!MyUtility.hash256(this.oldPassword).equals(account.getPassword())) {
                MyUtility.showError(PASSWORD_ERROR);
                return null;
            }

            account.setPassword(MyUtility.hash256(newPassword));
        } catch (NoSuchAlgorithmException ex) {
            java.util.logging.Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE, null, ex);
        }
        account = accountBean.updateAccount(account);
        //MyUtility.showMessage(CHANGE_PASSWORD_SUCCESS);
        return "/secure/editAccount?faces-redirect=true&message=" + CHANGE_PASSWORD_SUCCESS;
    }

    public void showMessage(String message) {
        MyUtility.showMessage(message);
    }

    /**
     * This method is to return updateAccount form
     *
     * @return string the link of edit account form
     */
    public String getUpdateAccountForm() {
        return "/secure/editAccount?faces-redirect=true";
    }

    /**
     * This method is to compare input user information with database user
     * information.
     *
     * @return redirect to resetPassword web page if all information is matched.
     */
    public String forgotPassword() {
        boolean matchInfo = true;
        Account dbAccount = new Account();

        //Check input username
        dbAccount = accountBean.find(account.getEmail());
        if (dbAccount == null) {
            MyUtility.showError("invalid username.");
            return null;
        }

        //Compare input fullname with DB
        if (!dbAccount.getFullName().equals(account.getFullName())) {
            matchInfo = false;
            MyUtility.showError("Fullname is not match.");
        }

        //Compare input email with DB
        if (!dbAccount.getEmail().equals(account.getEmail())) {
            matchInfo = false;
            MyUtility.showError("Email is not match.");
        }

        //Compare input mobile with DB
        if (!dbAccount.getMobile().equals(account.getMobile())) {
            matchInfo = false;
            MyUtility.showError("Mobile is not match.");
        }

        if (matchInfo) {
            resetPassword(dbAccount);
            //reset input account information
            account = new Account();
            return "/resetPassword?faces-redirect=true";
        } else {
            //reset input account information
            account = new Account();
            return null;
        }
    }

    /**
     * Method to reset user Password Get new password, Update it into database
     * and send email to user email
     *
     * @param dbAccount as user account information
     */
    public void resetPassword(Account dbAccount) {
        newPassword = MyUtility.randomString(LENGTH_PW);

        System.out.println("New Password = " + newPassword);

        try {
            //Update new Password on Database
            dbAccount.setPassword(MyUtility.hash256(newPassword));
            account = accountBean.updateAccount(dbAccount);

            //Send email with new password to user email
            String msg = MyUtility.resetPwMessage(newPassword);
            try {
                MyUtility.sendEmail(account.getEmail(), "SharingBook reset password", msg);
            } catch (MessagingException ex) {
                java.util.logging.Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (NoSuchAlgorithmException ex) {
            java.util.logging.Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
