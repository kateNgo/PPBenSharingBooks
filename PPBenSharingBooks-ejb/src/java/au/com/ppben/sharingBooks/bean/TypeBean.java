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
import au.com.ppben.sharingBooks.domain.Type;
import java.io.Serializable;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import au.com.ppben.sharingBooks.remote.TypeBeanRemote;

@Stateless
public class TypeBean implements Serializable, TypeBeanRemote {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Type addType(Type type) {
        em.persist(type);
        em.flush();
        return type;
    }

    @Override
    public Type getType(long id) {
        Type type = em.find(Type.class, id);
        return type;
    }

    @Override
    public List<Type> list() {
        TypedQuery<Type> query = em.createQuery("select t from Type t", Type.class);
        return query.getResultList();
    }

}
