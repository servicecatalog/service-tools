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
 * Component exception for cache events (e.g. data not modified)
 * 
 * @author miethaner
 */
public class CacheException extends ServiceException {

    private static final long serialVersionUID = -5288079064424707113L;

    /**
     * Creates new cache exception
     * 
     * @param errorKey
     *            the enum key for the error
     * @param values
     *            the values for message placeholders
     */
    public CacheException(ErrorKey errorKey, String... values) {
        super(errorKey, values);
    }

    /**
     * Creates new cache exception
     * 
     * @param errorKey
     *            the enum key for the error
     * @param e
     *            the causing exception.
     * @param values
     *            the values for message placeholders
     */
    public CacheException(ErrorKey errorKey, Throwable e, String... values) {
        super(errorKey, e, values);
    }
}
