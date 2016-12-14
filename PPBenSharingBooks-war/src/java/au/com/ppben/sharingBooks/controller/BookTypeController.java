/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.ppben.sharingBooks.controller;

import au.com.ppben.sharingBooks.domain.BookType;
import au.com.ppben.sharingBooks.remote.BookTypeBeanRemote;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.jboss.logging.Logger;

/**
 *
 * @author phuong
 */
@Named
@SessionScoped
public class BookTypeController implements Serializable{
     Logger log = Logger.getLogger(this.getClass().getName());
    private BookType bookType;
    
    @EJB
    private BookTypeBeanRemote bookTypeBean ;
    
    public List<BookType> list(){
        return bookTypeBean.list();
    }
    
}
