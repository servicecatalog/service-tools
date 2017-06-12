/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 6, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.data;

import java.util.UUID;

import org.oscm.common.interfaces.enums.Operation;
import org.oscm.common.interfaces.exceptions.ServiceException;
import org.oscm.common.interfaces.keys.ActivityKey;

import com.google.gson.annotations.SerializedName;

/**
 * @author miethaner
 *
 */
public abstract class Event extends VersionedEntity {

    public static final String FIELD_ID = "id";
    public static final String FIELD_ETAG = "etag";
    public static final String FIELD_OPERATION = "operation";

    @SerializedName(FIELD_ID)
    private UUID id;

    @SerializedName(FIELD_ETAG)
    private UUID etag;

    @SerializedName(FIELD_OPERATION)
    private Operation operation;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getETag() {
        return etag;
    }

    public void setETag(UUID tag) {
        this.etag = tag;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public abstract void validateFor(ActivityKey activity)
            throws ServiceException;
}
