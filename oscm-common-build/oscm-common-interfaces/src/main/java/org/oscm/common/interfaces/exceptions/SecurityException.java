/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jul 13, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.exceptions;

/**
 * Component exception for security events and errors (e.g. permissions)
 * 
 * @author miethaner
 */
public class SecurityException extends ComponentException {

    private static final long serialVersionUID = 6780718172186488863L;

    /**
     * Creates new security exception
     * 
     * @param error
     *            the error code
     * @param message
     *            the error message
     */
    public SecurityException(Integer error, String message, String moreInfo) {
        super(error, message, moreInfo);
    }

    /**
     * Creates new security exception
     * 
     * @param error
     *            the error code
     * @param message
     *            the error message
     * @param moreInfo
     *            the additional information
     */
    public SecurityException(Integer error, String message, Throwable e) {
        super(error, message, e);
    }

    /**
     * Creates new security exception
     * 
     * @param error
     *            the error code
     * @param message
     *            the error message
     * @param e
     *            the causing exception. Its message will be saved as additional
     *            information
     */
    public SecurityException(Integer error, String message) {
        super(error, message);
    }

    /**
     * Creates new security exception
     * 
     * @param error
     *            the error code
     * @param e
     *            the causing exception. Its message will be reused.
     */
    public SecurityException(Integer error, Throwable e) {
        super(error, e);
    }
}
