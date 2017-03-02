/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.ppben.sharingBooks.remote;

import au.com.ppben.sharingBooks.domain.SubType;
import java.util.List;

/**
 *
 * @author phuong
 */
public interface SubTypeBeanRemote {

    public SubType addSubType(SubType subType);

    public SubType getSubType(long id);

    public List<SubType> list();

    public List<SubType> getSubTypesByType(long typeId);
}
