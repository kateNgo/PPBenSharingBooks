/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.ppben.sharingBooks.converter;

import au.com.ppben.sharingBooks.domain.Type;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;
import au.com.ppben.sharingBooks.remote.TypeBeanRemote;

/**
 *
 * @author phuong
 */
@Named
@SessionScoped
public class BookTypeConverter implements Converter, Serializable{

    @EJB
    private TypeBeanRemote bookTypeBean;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
       return bookTypeBean.getType(Integer.parseInt(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return (value).toString();
    }
    
}
