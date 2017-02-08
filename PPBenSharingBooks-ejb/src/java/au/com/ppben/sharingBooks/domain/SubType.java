/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.ppben.sharingBooks.domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
/**
 *
 * @author phuong
 */
@Entity
@NamedQueries({
    @NamedQuery(
            name = "searchSubTypesByType",
            query = "select st from SubType st where st.type.typeId = :typeId"),
    @NamedQuery(
            name = "loadAllSubTypes",
            query = "select st from SubType st ")
})
public class SubType implements Serializable{
     /**
     * This is primary key that auto increment 
     */
    @Id 
    @GeneratedValue
    private Long subTypeId;
    
    @Column
    @NotNull
    private String subTypeName;
    
    @ManyToOne
    private Type type;
    
    @OneToMany(mappedBy = "subType",orphanRemoval=true, cascade={CascadeType.ALL})
    List<Book> books;

    public SubType() {
    }

    public Long getSubTypeId() {
        return subTypeId;
    }

    public void setSubTypeId(Long subTypeId) {
        this.subTypeId = subTypeId;
    }

    

    public String getSubTypeName() {
        return subTypeName;
    }

    public void setSubTypeName(String subTypeName) {
        this.subTypeName = subTypeName;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    

    
}
