/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: May 12, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest;

import javax.validation.ValidationException;

import org.oscm.common.interfaces.data.DataType;
import org.oscm.common.interfaces.data.Versionable;
import org.oscm.common.interfaces.enums.Operation;
import org.oscm.common.interfaces.exceptions.ServiceException;

import com.google.gson.annotations.SerializedName;

/**
 * Super class for rest representations
 * 
 * @author miethaner
 */
public abstract class Representation implements DataType, Versionable {

    public static final String FIELD_ID = "id";
    public static final String FIELD_ETAG = "etag";

    private transient Integer version;

    @SerializedName(FIELD_ID)
    private Long id;

    @SerializedName(FIELD_ETAG)
    private Long etag;

    public Representation() {
    }

    public Representation(DataType data) {
        if (data == null) {
            return;
        }

        this.id = data.getId();
        this.etag = data.getETag();
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getETag() {
        return etag;
    }

    public void setETag(Long tag) {
        this.etag = tag;
    }

    @Override
    public Operation getLastOperation() {
        return null;
    }

    /**
     * Validates the content and format of the fields to be legitimate for a
     * create.
     * 
     * @throws ValidationException
     *             if not valid.
     */
    public abstract void validateCreate() throws ServiceException;

    /**
     * Validates the content and format of the fields to be legitimate for an
     * update.
     * 
     * @throws ValidationException
     *             if not valid.
     */
    public abstract void validateUpdate() throws ServiceException;

}
