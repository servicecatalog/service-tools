/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: May 12, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.kafka;

import org.oscm.common.interfaces.data.DataType;
import org.oscm.common.interfaces.data.Versionable;
import org.oscm.common.interfaces.enums.Operation;

import com.google.gson.annotations.SerializedName;

/**
 * Base class for kafka representations
 * 
 * @author miethaner
 */
public abstract class Representation implements DataType, Versionable {

    public static final String FIELD_ID = "id";
    public static final String FIELD_ETAG = "etag";
    public static final String FIELD_VERSION = "version";
    public static final String FIELD_LAST_OPERATION = "last_operation";

    @SerializedName(FIELD_VERSION)
    private Integer version;

    @SerializedName(FIELD_ID)
    private Long id;

    @SerializedName(FIELD_ETAG)
    private Long etag;

    @SerializedName(FIELD_LAST_OPERATION)
    private Operation lastOperation;

    public Representation() {
    }

    public Representation(DataType data) {
        if (data == null) {
            return;
        }

        this.id = data.getId();
        this.etag = data.getETag();
        this.lastOperation = data.getLastOperation();
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public Operation getLastOperation() {
        return lastOperation;
    }

    public void setLastOperation(Operation operation) {
        this.lastOperation = operation;
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

    public void setETag(Long etag) {
        this.etag = etag;
    }
}
