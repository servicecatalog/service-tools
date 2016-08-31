/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: May 9, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest;

import java.util.Collection;

import javax.ws.rs.WebApplicationException;

import org.oscm.common.interfaces.exceptions.ComponentException;

import com.google.gson.annotations.SerializedName;

/**
 * Generic representation class for collections of representation items
 * 
 * @author miethaner
 */
public class RCollection<T extends Representation> extends Representation {

    public static final String FIELD_ITEMS = "items";

    @SerializedName(FIELD_ITEMS)
    private Collection<T> items;

    public Collection<T> getItems() {
        return items;
    }

    public void setItems(Collection<T> items) {
        this.items = items;
    }

    public RCollection() {
        super(null);
    }

    public RCollection(Collection<T> items) {
        super(null);
        this.items = items;
    }

    @Override
    public void validateContent() throws WebApplicationException,
            ComponentException {

        if (items != null) {
            for (T item : items) {
                item.validateContent();
            }
        }
    }

    @Override
    public void setVersion(Integer version) {
        super.setVersion(version);

        for (T item : items) {
            item.setVersion(version);
        }
    }

    @Override
    public void update() {

        for (T item : items) {
            item.update();
        }

        // nothing to update
    }

    @Override
    public void convert() {

        for (T item : items) {
            item.convert();
        }

        // nothing to convert
    }

}
