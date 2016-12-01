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
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

/**
 *
 * @author phuong
 */
@Entity
public class BookType implements Serializable{
    /**
     * This is primary key that auto increment 
     */
    @Id 
    @GeneratedValue
    private Long typeId;
    
    @Column
    @NotNull
    private String typeName;
     @OneToMany(mappedBy = "type",orphanRemoval=true, cascade={CascadeType.ALL})
    List<Book> books;

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    
    
    
}
