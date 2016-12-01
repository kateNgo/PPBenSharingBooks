/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.ppben.sharingBooks.remote;


import au.com.ppben.sharingBooks.domain.Book;
import java.util.List;

/**
 *
 * @author phuong
 */
public interface BookBeanRemote {
    /**
     * This method is to update a book
     * 
     * @param book the updated book
     */
     public void update(Book book);
      /**
     * This method is to delete a book into book table
     *
     * @param id that is the book id to delete
     * 
     */
    public void delete(long id);
     /**
     * This method is to add a new record into book table
     *
     * @param book that is the book to add
     * @return book the new added book
     */
    public Book addNew(Book book);
    
    /**
     * this method is to get book by id
     *
     * @param id
     * @return book is found or null
     */
    public Book getBookbyId(long id);
     /**
     * This method is to list all books in book table
     *
     * @return List list of books
     */
    public List<Book> allBooks();
     /**
     * This method is to list books by title, authors and publisher in book
     * table
     *
     * @param title
     *
     * @return List list of book found by conditions
     
     */
    
    public List<Book> searchBooksByTitle(String title);

    
    
}
