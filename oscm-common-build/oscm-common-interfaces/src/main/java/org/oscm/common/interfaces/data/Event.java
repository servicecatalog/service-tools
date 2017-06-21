/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 6, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.data;

import java.util.UUID;

import org.oscm.common.interfaces.enums.Messages;
import org.oscm.common.interfaces.enums.Operation;
import org.oscm.common.interfaces.exceptions.ServiceException;
import org.oscm.common.interfaces.exceptions.ValidationException;
import org.oscm.common.interfaces.keys.ActivityKey;

import com.google.gson.annotations.SerializedName;

/**
 * Entity class for events.
 * 
 * @author miethaner
 */
public abstract class Event extends VersionedEntity {

    public static final String FIELD_ID = "id";
    public static final String FIELD_ETAG = "etag";
    public static final String FIELD_OPERATION = "operation";
    public static final String FIELD_LINKS = "links";

    @SerializedName(FIELD_ID)
    private UUID id;

    @SerializedName(FIELD_ETAG)
    private UUID etag;

    @SerializedName(FIELD_OPERATION)
    private Operation operation;

    /**
     * Gets the event id. Returns null if not set.
     * 
     * @return the id or null
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the event id.
     * 
     * @param id
     *            the event id
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Gets the etag for this event. Returns null if not set.
     * 
     * @return the etag or null
     */
    public UUID getETag() {
        return etag;
    }

    /**
     * Sets the etag for this event.
     * 
     * @param tag
     *            the etag
     */
    public void setETag(UUID tag) {
        this.etag = tag;
    }

    /**
     * Gets the operation for this event. Returns null if not set.
     * 
     * @return the operation or null
     */
    public Operation getOperation() {
        return operation;
    }

    /**
     * Sets the operation for this event.
     * 
     * @param operation
     *            the operation
     */
    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    /**
     * Validates all fields for the given activity key (command or query).
     * 
     * @param activity
     *            the command or query key
     * @throws ValidationException
     *             if a field is missing or in a wrong format.
     */
    public abstract void validateFor(ActivityKey activity)
            throws ServiceException;

    protected void validateId() throws ServiceException {
        if (id == null) {
            throw new ValidationException(
                    Messages.ERROR_MANDATORY_PROPERTY_NOT_PRESENT, FIELD_ID);
        }
    }

    protected void validateETag() throws ServiceException {
        if (etag == null) {
            throw new ValidationException(
                    Messages.ERROR_MANDATORY_PROPERTY_NOT_PRESENT, FIELD_ETAG);
        }
    }
}
