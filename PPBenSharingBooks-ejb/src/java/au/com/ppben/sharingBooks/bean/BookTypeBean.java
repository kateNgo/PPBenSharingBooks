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
import au.com.ppben.sharingBooks.domain.BookType;
import au.com.ppben.sharingBooks.remote.BookTypeBeanRemote;
import java.io.Serializable;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class BookTypeBean implements Serializable, BookTypeBeanRemote{

    @PersistenceContext
    private EntityManager em;
    
    @Override
    public BookType addBookType(BookType bookType) {
        em.persist(bookType);
        em.flush();
        return bookType;
    }

    @Override
    public BookType getBookType(long id) {
        BookType type = em.find(BookType.class, id);
        return type;
    }

    @Override
    public List<BookType> list() {
        TypedQuery<BookType> query = em.createQuery("select t from BookType t", BookType.class);
        return query.getResultList();
    }
    
}
