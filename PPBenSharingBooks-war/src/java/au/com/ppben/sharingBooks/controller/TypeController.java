/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.ppben.sharingBooks.controller;

import au.com.ppben.sharingBooks.domain.Type;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.jboss.logging.Logger;
import au.com.ppben.sharingBooks.remote.TypeBeanRemote;

/**
 *
 * @author phuong
 */
@Named
@SessionScoped
public class TypeController implements Serializable {

    Logger log = Logger.getLogger(this.getClass().getName());
    private Type type;

    @EJB
    private TypeBeanRemote typeBean;

    public List<Type> list() {
        return typeBean.list();
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

}
