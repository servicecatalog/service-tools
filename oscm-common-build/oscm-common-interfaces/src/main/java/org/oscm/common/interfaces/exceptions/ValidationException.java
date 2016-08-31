/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jun 23, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.exceptions;

/**
 * Component exception for validation events and errors
 * 
 * @author miethaner
 */
public class ValidationException extends ComponentException {

    private static final long serialVersionUID = 971181144195903482L;

    private String property;

    /**
     * Creates new validation exception
     * 
     * @param error
     *            the error code
     * @param message
     *            the error message
     */
    public ValidationException(Integer error, String message) {
        super(error, message);
    }

    /**
     * Creates new validation exception
     * 
     * @param error
     *            the error code
     * @param e
     *            the causing exception. Its message will be reused.
     */
    public ValidationException(Integer error, Throwable e) {
        super(error, e);
    }

    /**
     * Creates new validation exception
     * 
     * @param error
     *            the error code
     * @param property
     *            the property name
     * @param message
     *            the error message
     */
    public ValidationException(Integer error, String property, String message) {
        super(error, message);
        this.property = property;
    }

    /**
     * Creates new validation exception
     * 
     * @param error
     *            the error code
     * @param message
     *            the error message
     * @param e
     *            the causing exception. Its message will be saved as additional
     *            information
     */
    public ValidationException(Integer error, String message, Throwable e) {
        super(error, message, e);
    }

    /**
     * Creates new validation exception
     * 
     * @param error
     *            the error code
     * @param property
     *            the property name
     * @param message
     *            the error message
     * @param e
     *            the causing exception. Its message will be saved as additional
     *            information
     */
    public ValidationException(Integer error, String property, String message,
            Throwable e) {
        super(error, message, e);
        this.property = property;
    }

    /**
     * Creates new validation exception
     * 
     * @param error
     *            the error code
     * @param property
     *            the property name
     * @param message
     *            the error message
     * @param moreInfo
     *            the additional information
     */
    public ValidationException(Integer error, String property, String message,
            String moreInfo) {
        super(error, message, moreInfo);
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
}
