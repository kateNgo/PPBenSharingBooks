/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.ppben.sharingBooks.bean;

import au.com.ppben.sharingBooks.domain.SubType;
import au.com.ppben.sharingBooks.remote.SubTypeBeanRemote;
import java.io.Serializable;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author Phuong Ngo
 */
@Stateless
public class SubTypeBean implements Serializable, SubTypeBeanRemote {

    @PersistenceContext
    private EntityManager em;

    @Override
    public SubType addSubType(SubType subType) {
        em.persist(subType);
        em.flush();
        return subType;
    }

    @Override
    public SubType getSubType(long id) {
        return em.find(SubType.class, id);
    }

    @Override
    public List<SubType> list() {
        TypedQuery<SubType> query = em.createNamedQuery("loadAllSubTypes", SubType.class);
        return query.getResultList();
    }

    @Override
    public List<SubType> getSubTypesByType(long typeId) {
        TypedQuery<SubType> query = em.createNamedQuery("searchSubTypesByType", SubType.class);
        query.setParameter("typeId", typeId);
        return query.getResultList();

    }

}
