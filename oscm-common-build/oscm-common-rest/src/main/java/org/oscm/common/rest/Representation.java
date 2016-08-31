/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: May 12, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest;

import org.oscm.common.interfaces.data.DataType;
import org.oscm.common.interfaces.enums.Operation;
import org.oscm.common.interfaces.exceptions.ComponentException;

import com.google.gson.annotations.SerializedName;

/**
 * Super class for rest representations
 * 
 * @author miethaner
 */
public abstract class Representation implements DataType {

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
     * Validates the content and format of the fields to be legitimate. Throws
     * BadRequestException if not valid.
     * 
     * @throws ComponentException
     */
    public abstract void validateContent() throws ComponentException;

    /**
     * Updates the fields and format of the internal version to the current one
     */
    public abstract void update();

    /**
     * Converts the format and fields of the current version to the internal old
     * one
     */
    public abstract void convert();
}
