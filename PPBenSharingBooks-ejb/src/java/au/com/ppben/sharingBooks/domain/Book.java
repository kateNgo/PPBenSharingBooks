/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template bookFile, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.ppben.sharingBooks.domain;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Thi Ngo
 * 
 * This class presents rows in the book table
 * 
 */

@Entity
@NamedQueries({
    @NamedQuery(
            name = "loadAllBooks",
            query = "select b from Book b"),
     @NamedQuery(
            name = "searchBooks",
            query = "select b from Book b where b.authors like :authors  and b.publisher like :publisher and b.title like :title"),
     @NamedQuery(
            name = "searchBooksByTitle",
            query = "select b from Book b where lower(b.title) like :title")
})
public class Book implements Serializable{
    
    /**
     * This is primary key that auto increment 
     */
    @Id 
    @GeneratedValue
    private Long bookId;
    
    @Column
    @NotNull
    private String title;
    @Column
    private String authors;
    @Column
    private Integer publishYear;
    @Column
    private String publisher;
    @Column
    @NotNull
    private String bookFile;
    @ManyToOne
    private Account account;
    @ManyToOne
    private BookType type;
    
   
    /**
     * This is default constructor, to make it to be a java bean
     * 
     */
    public Book() {
    }
    
    /**
     * This method is to get the value for account field that represents for account who owns the uploaded book
     * @return String that is the user name 's value. This is required field.
     */
    
    public Account getAccount() {
        return account;
    }
    /**
     * This method is to set the value for username field that represents for member who owns the uploaded book
     * @param account 
     */
    public void setAccount(Account account) {
        this.account = account;
    }
    /**
     * This method is to get id value of book
     * @return Long that is the ID of the book
    */
    public Long getBookId() {
        return bookId;
    }
    /**
     * This method is to set value of ID of book
     * @param bookId 
     */
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
    /**
     * This method is to get the title 's value of book
     * @return String is title 's value
     */
    public String getTitle() {
        return title;
    }
    /**
     * This method is to set the value of title of book
     * @param title is the string value of title. This is not allowed null or empty.
    */
    public void setTitle(String title) {
       
        this.title = title;
       
    }
    /**
     * This method is to get the authors 's value of book
     * @return String is authors 's value
     */
    public String getAuthors() {
        return authors;
    }
    /**
     * This method is to set the value of authors of book
     * @param authors is the string value of authors. 
    */
    public void setAuthors(String authors) {
        this.authors = authors;
    }
    /**
     * This method is to get the publisher 's value of book
     * @return String is publisher 's value
     */
    public String getPublisher() {
        return publisher;
    }
    /**
     * This method is to set the value of publisher of book
     * @param publisher is the string value of publisher. 
    */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    /**
     * This method is to get the publish year 's value of book
     * @return integer is publish year 's value
     */
    public Integer getPublishYear() {
        return publishYear;
    }
    /**
     * This method is to set the value of authors of book
     * @param publishYear is the integer value of publish year. 
    */
    public void setPublishYear(Integer publishYear) {
        this.publishYear = publishYear;
    }
    /**
     * This method is to get the file name of book
     * @return String is file name of book
     */
    public String getBookFile() {
        return bookFile;
    }
    /**
     * This method is to set file name of the uploaded book. 
     * @param bookFile that is not allowed null or empty value
    */
    public void setBookFile(String bookFile) {
        this.bookFile = bookFile;
    }

    /**
     * This is bookType attribute of the book
     * @return bookType the type of book
     */
    public BookType getType() {
        return type;
    }

    public void setType(BookType type) {
        this.type = type;
    }
    
    
    @Override
    public String toString(){
        return (title +", " + authors+", " + publisher);
    }
    
}
