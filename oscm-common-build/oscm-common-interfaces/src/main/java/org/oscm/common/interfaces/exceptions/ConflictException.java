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
 * Component exception for conflict events and errors (e.g. violation of a
 * constraint)
 * 
 * @author miethaner
 */
public class ConflictException extends ServiceException {

    private static final long serialVersionUID = 5446563151469012266L;

    /**
     * Creates new conflict exception
     * 
     * @param errorKey
     *            the enum key for the error
     * @param values
     *            the values for message placeholders
     */
    public ConflictException(ErrorKey errorKey, String... values) {
        super(errorKey, values);
    }

    /**
     * Creates new conflict exception
     * 
     * @param errorKey
     *            the enum key for the error
     * @param e
     *            the causing exception.
     * @param values
     *            the values for message placeholders
     */
    public ConflictException(ErrorKey errorKey, Throwable e, String... values) {
        super(errorKey, e, values);
    }
}
