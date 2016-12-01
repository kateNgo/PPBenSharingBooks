
package au.com.ppben.sharingBooks.bean;


import au.com.ppben.sharingBooks.MyUtility;
import au.com.ppben.sharingBooks.domain.Book;
import au.com.ppben.sharingBooks.remote.BookBeanRemote;
import java.io.Serializable;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author phuong ngo
 */

@Stateless
public class BookBean implements Serializable, BookBeanRemote{
    @PersistenceContext
    private EntityManager em;
    
    @Override
    public void update(Book book){
        em.merge(book);
        em.flush();
    }
     
    @Override
    public void delete(long id){
        Book book = getBookbyId(id);
        em.remove(book);
        em.flush();
    }
     
    @Override
    public Book addNew(Book book){
        em.persist(book);
        em.flush();
        return book;
    }
    
    @Override
    public Book getBookbyId(long id){
        return em.find(Book.class, id);
    }
   
    @Override
    public List<Book> allBooks(){
        TypedQuery<Book> query = em.createNamedQuery("loadAllBooks",Book.class);
        return query.getResultList();
    }
    
    @Override
    public List<Book> searchBooksByTitle(String title) {
        TypedQuery<Book> query = em.createNamedQuery("searchBooksByTitle",Book.class);
         if (!MyUtility.isEmptyString(title)){
             query.setParameter("title", "%"+title.toLowerCase()+"%");
         }else{
             query.setParameter("title", "%");
         }      
         
        return query.getResultList();
    }
}
