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
    public static final String FIELD_OPERATION = "operation";
    public static final String FIELD_LINKS = "links";

    @SerializedName(FIELD_ID)
    private UUID id;

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

    /**
     * Validates the id if it is present or not. If the given flag is true, a
     * exception will be thrown in case the id is null and vice versa.
     * 
     * @param present
     *            true if id should be present
     * @throws ValidationException
     *             if conditions are not met
     */
    protected void validateId(boolean present) throws ServiceException {
        if (present) {
            if (id == null) {
                throw new ValidationException(
                        Messages.ERROR_MANDATORY_PROPERTY_NOT_PRESENT, FIELD_ID,
                        FIELD_ID);
            }
        } else {
            if (id != null) {
                throw new ValidationException(Messages.ERROR_BAD_PROPERTY,
                        FIELD_ID, FIELD_ID);
            }
        }
    }
}
