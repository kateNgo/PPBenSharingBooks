/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.ppben.sharingBooks.controller;

import au.com.ppben.sharingBooks.domain.SubType;
import au.com.ppben.sharingBooks.remote.SubTypeBeanRemote;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author phuong
 */
@Named
@SessionScoped
public class SubTypeController implements Serializable {
    @EJB
    private SubTypeBeanRemote subTypeBean;
    
    private SubType subType;

    public SubType getSubType() {
        return subType;
    }

    public void setSubType(SubType subType) {
        this.subType = subType;
    }
    public List<SubType> getSubTypesByType(long typeId){
        return subTypeBean.getSubTypesByType(typeId);
    }
    public List<SubType> list(){
        return subTypeBean.list();
    }
    
}
