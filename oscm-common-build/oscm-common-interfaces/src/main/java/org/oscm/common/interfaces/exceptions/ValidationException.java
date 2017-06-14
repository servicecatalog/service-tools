/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jun 23, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.exceptions;

import org.oscm.common.interfaces.data.Failure;
import org.oscm.common.interfaces.enums.Reason;
import org.oscm.common.interfaces.keys.MessageKey;

/**
 * Service exception for validation events and errors
 * 
 * @author miethaner
 */
public class ValidationException extends ServiceException {

    private static final long serialVersionUID = 971181144195903482L;

    private String property;

    /**
     * Creates new validation exception
     * 
     * @param messageKey
     *            the enum key for the message
     * @param property
     *            the property name
     * @param values
     *            the values for message placeholders
     */
    public ValidationException(MessageKey messageKey, String property,
            String... values) {
        super(messageKey, values);
        this.property = property;
    }

    /**
     * Creates new validation exception
     * 
     * @param messageKey
     *            the enum key for the message
     * @param property
     *            the property name
     * @param e
     *            the causing exception.
     * @param values
     *            the values for message placeholders
     */
    public ValidationException(MessageKey messageKey, String property,
            Throwable e, String... values) {
        super(messageKey, e, values);
        this.property = property;
    }

    /**
     * Gets the name of the causing property. Returns null if not set.
     * 
     * @return the property name or null
     */
    public String getProperty() {
        return property;
    }

    /**
     * Sets the name of the causing property.
     * 
     * @param property
     *            the property name. Can be null.
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
        Failure f = super.getAsFailure();
        f.setProperty(property);

        return f;
    }
}
