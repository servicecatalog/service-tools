/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jun 23, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.exceptions;

/**
 * Component exception for cache events (e.g. data not modified)
 * 
 * @author miethaner
 */
public class CacheException extends ComponentException {

    private static final long serialVersionUID = -5288079064424707113L;

    /**
     * Creates new cache exception
     * 
     * @param error
     *            the error code
     * @param message
     *            the error message
     */
    public CacheException(Integer error, String message) {
        super(error, message);
    }

    /**
     * Creates new cache exception
     * 
     * @param error
     *            the error code
     * @param message
     *            the error message
     * @param moreInfo
     *            the additional information
     */
    public CacheException(Integer error, String message, String moreInfo) {
        super(error, message, moreInfo);
    }

    /**
     * Creates new cache exception
     * 
     * @param error
     *            the error code
     * @param message
     *            the error message
     * @param e
     *            the causing exception. Its message will be saved as additional
     *            information
     */
    public CacheException(Integer error, String message, Throwable e) {
        super(error, message, e);
    }

    /**
     * Creates new cache exception
     * 
     * @param error
     *            the error code
     * @param e
     *            the causing exception. Its message will be reused.
     */
    public CacheException(Integer error, Throwable e) {
        super(error, e);
    }
}
