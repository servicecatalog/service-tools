/*******************************************************************************
 *
 *  Copyright FUJITSU LIMITED 2016                                           
 *
 *  Creation Date: Jun 22, 2016                                                      
 *
 *******************************************************************************/

package org.oscm.lagom.exceptions;

import org.oscm.lagom.data.Failure;
import org.oscm.lagom.enums.Reason;
import org.oscm.lagom.interfaces.MessageKey;

import java.util.UUID;

/**
 * Super class for all service specific exceptions
 *
 * @author miethaner
 */
public abstract class ServiceException extends Exception {

    private static final long serialVersionUID = -4770733030290897811L;

    private UUID id;
    private Integer code;

    /**
     * Creates new service exception
     *
     * @param messageKey the enum key for the message
     * @param values     the values for message placeholders
     */
    public ServiceException(MessageKey messageKey, String... values) {
        super(messageKey.getMessage(values));
        this.id = UUID.randomUUID();
        this.code = messageKey.getCode();
    }

    /**
     * Creates new service exception
     *
     * @param messageKey the enum key for the message
     * @param e          the causing exception.
     * @param values     the values for message placeholders
     */
    public ServiceException(MessageKey messageKey, Throwable e,
        String... values) {
        super(messageKey.getMessage(values), e);
        this.id = UUID.randomUUID();
        this.code = messageKey.getCode();
    }

    /**
     * Gets the unique identifier for this exception.
     *
     * @return the exception id
     */
    public UUID getId() {
        return id;
    }

    /**
     * Gets the error code. Returns null if not set.
     *
     * @return the error code or null
     */
    public Integer getCode() {
        return code;
    }

    /**
     * Gets the reason category for this exception.
     *
     * @return the reason enum
     */
    public abstract Reason getReason();

    /**
     * Gets the Exception as {@link Failure} for results.
     *
     * @return the failure
     */
    public Failure getAsFailure() {
        return new Failure(getReason(), id, code, null, getMessage());
    }
}
