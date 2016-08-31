/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jun 23, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.exceptions;

/**
 * Component exception for unexpected internal errors
 * 
 * @author miethaner
 */
public class InternalException extends ComponentException {

    private static final long serialVersionUID = 1397618428214541396L;

    /**
     * Creates new internal exception
     * 
     * @param error
     *            the error code
     * @param message
     *            the error message
     */
    public InternalException(Integer error, String message, String moreInfo) {
        super(error, message, moreInfo);
    }

    /**
     * Creates new internal exception
     * 
     * @param error
     *            the error code
     * @param message
     *            the error message
     * @param moreInfo
     *            the additional information
     */
    public InternalException(Integer error, String message, Throwable e) {
        super(error, message, e);
    }

    /**
     * Creates new internal exception
     * 
     * @param error
     *            the error code
     * @param message
     *            the error message
     * @param e
     *            the causing exception. Its message will be saved as additional
     *            information
     */
    public InternalException(Integer error, String message) {
        super(error, message);
    }

    /**
     * Creates new internal exception
     * 
     * @param error
     *            the error code
     * @param e
     *            the causing exception. Its message will be reused.
     */
    public InternalException(Integer error, Throwable e) {
        super(error, e);
    }
}
