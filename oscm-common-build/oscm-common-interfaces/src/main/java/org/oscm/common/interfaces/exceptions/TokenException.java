/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jul 13, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.exceptions;

/**
 * Component exception for security token events and errors (e.g. token missing)
 * 
 * @author miethaner
 */
public class TokenException extends ComponentException {

    private static final long serialVersionUID = -4196103934993229424L;

    /**
     * Creates new token exception
     * 
     * @param error
     *            the error code
     * @param message
     *            the error message
     */
    public TokenException(Integer error, String message, String moreInfo) {
        super(error, message, moreInfo);
    }

    /**
     * Creates new token exception
     * 
     * @param error
     *            the error code
     * @param message
     *            the error message
     * @param moreInfo
     *            the additional information
     */
    public TokenException(Integer error, String message, Throwable e) {
        super(error, message, e);
    }

    /**
     * Creates new token exception
     * 
     * @param error
     *            the error code
     * @param message
     *            the error message
     * @param e
     *            the causing exception. Its message will be saved as additional
     *            information
     */
    public TokenException(Integer error, String message) {
        super(error, message);
    }

    /**
     * Creates new token exception
     * 
     * @param error
     *            the error code
     * @param e
     *            the causing exception. Its message will be reused.
     */
    public TokenException(Integer error, Throwable e) {
        super(error, e);
    }
}
