/*******************************************************************************
 *
 *  Copyright FUJITSU LIMITED 2016                                           
 *
 *  Creation Date: Jun 23, 2016                                                      
 *
 *******************************************************************************/

package org.oscm.lagom.exceptions;

import org.oscm.lagom.data.Failure;
import org.oscm.lagom.enums.Reason;
import org.oscm.lagom.interfaces.MessageKey;

/**
 * Service exception for validation events and errors.
 */
public class ValidationException extends ServiceException {

    private static final long serialVersionUID = 971181144195903482L;

    private String property;

    /**
     * Creates a new validation exception.
     *
     * @param messageKey the enum key for the message
     * @param property   the property name
     * @param values     the values for message placeholders
     */
    public ValidationException(MessageKey messageKey, String property, String... values) {
        super(messageKey, values);
        this.property = property;
    }

    /**
     * Creates a new validation exception.
     *
     * @param messageKey the enum key for the message
     * @param property   the property name
     * @param e          the causing exception.
     * @param values     the values for message placeholders
     */
    public ValidationException(MessageKey messageKey, String property, Throwable e,
        String... values) {
        super(messageKey, e, values);
        this.property = property;
    }

    /**
     * Gets the name of the causing property.
     *
     * @return the property name, null if not set
     */
    public String getProperty() {
        return property;
    }

    /**
     * Sets the name of the causing property.
     *
     * @param property the property name, null returns null
     */
    public void setProperty(String property) {
        this.property = property;
    }

    @Override
    public Reason getReason() {
        return Reason.VALIDATION;
    }

    @Override
    public Failure getAsFailure() {
        return new Failure(getReason(), getId(), getCode(), property,
            getMessage());
    }
}
