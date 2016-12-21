/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jul 13, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.exceptions;

import org.oscm.common.interfaces.config.ErrorKey;

/**
 * Component exception for security token events and errors (e.g. token missing)
 * 
 * @author miethaner
 */
public class TokenException extends ServiceException {

    private static final long serialVersionUID = -4196103934993229424L;

    /**
     * Creates new token exception
     * 
     * @param errorKey
     *            the enum key for the error
     * @param values
     *            the values for message placeholders
     */
    public TokenException(ErrorKey errorKey, String... values) {
        super(errorKey, values);
    }

    /**
     * Creates new token exception
     * 
     * @param errorKey
     *            the enum key for the error
     * @param e
     *            the causing exception.
     * @param values
     *            the values for message placeholders
     */
    public TokenException(ErrorKey errorKey, Throwable e, String... values) {
        super(errorKey, e, values);
    }
}
