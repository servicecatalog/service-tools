/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jun 23, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.exceptions;

/**
 * Component exception for concurrency events and errors.
 * 
 * @author miethaner
 */
public class ConcurrencyException extends ComponentException {

    private static final long serialVersionUID = -4180662601244206044L;

    /**
     * Creates new concurrency exception
     * 
     * @param error
     *            the error code
     * @param message
     *            the error message
     */
    public ConcurrencyException(Integer error, String message) {
        super(error, message);
    }

    /**
     * Creates new concurrency exception
     * 
     * @param error
     *            the error code
     * @param message
     *            the error message
     * @param moreInfo
     *            the additional information
     */
    public ConcurrencyException(Integer error, String message, String moreInfo) {
        super(error, message, moreInfo);
    }

    /**
     * Creates new concurrency exception
     * 
     * @param error
     *            the error code
     * @param message
     *            the error message
     * @param e
     *            the causing exception. Its message will be saved as additional
     *            information
     */
    public ConcurrencyException(Integer error, String message, Throwable e) {
        super(error, message, e);
    }

    /**
     * Creates new concurrency exception
     * 
     * @param error
     *            the error code
     * @param e
     *            the causing exception. Its message will be reused.
     */
    public ConcurrencyException(Integer error, Throwable e) {
        super(error, e);
    }
}
