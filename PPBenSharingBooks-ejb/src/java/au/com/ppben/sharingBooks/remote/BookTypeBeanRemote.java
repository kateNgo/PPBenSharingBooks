/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.ppben.sharingBooks.remote;

/**
 *
 * @author phuong
 */
import au.com.ppben.sharingBooks.domain.BookType;
import java.util.List;

public interface BookTypeBeanRemote {
    
    /**
     * This method is to add a new bookType
     * @param bookType
     * @return bookType the new added bookType
     */
    public BookType addBookType(BookType bookType);
    
    /**
     * This method is to get a type by id
     * @param id
     * @return bookType the found bookType
     */
    public BookType getBookType(long id);
    
    /**
     * This method is to get all bookTypes
     * @return list the list of all bookTypes
     */
    public List<BookType> list();
    
}
