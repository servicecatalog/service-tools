/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jun 22, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.exceptions;

import org.oscm.common.interfaces.config.ErrorKey;

/**
 * Super class for all service specific exceptions
 * 
 * @author miethaner
 */
public class ServiceException extends Exception {

    private static final long serialVersionUID = -4770733030290897811L;

    private Integer error;

    /**
     * Creates new service exception
     * 
     * @param errorKey
     *            the enum key for the error
     * @param values
     *            the values for message placeholders
     */
    public ServiceException(ErrorKey errorKey, String... values) {
        super(errorKey.getMessage(values));
        this.error = errorKey.getId();
    }

    /**
     * Creates new service exception
     * 
     * @param errorKey
     *            the enum key for the error
     * @param e
     *            the causing exception.
     * @param values
     *            the values for message placeholders
     */
    public ServiceException(ErrorKey errorKey, Throwable e, String... values) {
        super(errorKey.getMessage(values), e);
        this.error = errorKey.getId();
    }

    /**
     * Gets the error code. Returns null if not set.
     * 
     * @return the error code or null
     */
    public Integer getError() {
        return error;
    }

    /**
     * Sets the error code.
     * 
     * @param error
     *            the error code. Can be null.
     */
    public void setError(Integer error) {
        this.error = error;
    }
}
