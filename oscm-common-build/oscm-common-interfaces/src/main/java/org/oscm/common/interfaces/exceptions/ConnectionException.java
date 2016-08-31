/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 24, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.exceptions;

/**
 * Connection exception for connectivity errors and events
 * 
 * @author miethaner
 */
public class ConnectionException extends ComponentException {

    private static final long serialVersionUID = 1981755503432130845L;

    /**
     * Creates new connection exception
     * 
     * @param error
     *            the error code
     * @param message
     *            the error message
     */
    public ConnectionException(Integer error, String message) {
        super(error, message);
    }

    /**
     * Creates new connection exception
     * 
     * @param error
     *            the error code
     * @param message
     *            the error message
     * @param moreInfo
     *            the additional information
     */
    public ConnectionException(Integer error, String message, String moreInfo) {
        super(error, message, moreInfo);
    }

    /**
     * Creates new connection exception
     * 
     * @param error
     *            the error code
     * @param message
     *            the error message
     * @param e
     *            the causing exception. Its message will be saved as additional
     *            information
     */
    public ConnectionException(Integer error, String message, Throwable e) {
        super(error, message, e);
    }

    /**
     * Creates new connection exception
     * 
     * @param error
     *            the error code
     * @param e
     *            the causing exception. Its message will be reused.
     */
    public ConnectionException(Integer error, Throwable e) {
        super(error, e);
    }
}
