/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jun 23, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.exceptions;

import org.oscm.common.interfaces.config.ErrorKey;

/**
 * Component exception for unexpected internal errors
 * 
 * @author miethaner
 */
public class InternalException extends ServiceException {

    private static final long serialVersionUID = 1397618428214541396L;

    /**
     * Creates new internal exception
     * 
     * @param errorKey
     *            the enum key for the error
     * @param values
     *            the values for message placeholders
     */
    public InternalException(ErrorKey errorKey, String... values) {
        super(errorKey, values);
    }

    /**
     * Creates new internal exception
     * 
     * @param errorKey
     *            the enum key for the error
     * @param e
     *            the causing exception.
     * @param values
     *            the values for message placeholders
     */
    public InternalException(ErrorKey errorKey, Throwable e, String... values) {
        super(errorKey, e, values);
    }
}
