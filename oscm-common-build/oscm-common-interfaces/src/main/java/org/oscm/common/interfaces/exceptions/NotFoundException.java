/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jun 24, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.exceptions;

import org.oscm.common.interfaces.config.ErrorKey;

/**
 * Component exception for not found events and errors (e.g. not found object)
 * 
 * @author miethaner
 */
public class NotFoundException extends ServiceException {

    private static final long serialVersionUID = -8581467028129479875L;

    /**
     * Creates new not found exception
     * 
     * @param errorKey
     *            the enum key for the error
     * @param values
     *            the values for message placeholders
     */
    public NotFoundException(ErrorKey errorKey, String... values) {
        super(errorKey, values);
    }

    /**
     * Creates new not found exception
     * 
     * @param errorKey
     *            the enum key for the error
     * @param e
     *            the causing exception.
     * @param values
     *            the values for message placeholders
     */
    public NotFoundException(ErrorKey errorKey, Throwable e, String... values) {
        super(errorKey, e, values);
    }
}
