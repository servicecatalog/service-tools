/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jun 24, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.exceptions;

/**
 * Component exception for not found events and errors (e.g. not found object)
 * 
 * @author miethaner
 */
public class NotFoundException extends ComponentException {

    private static final long serialVersionUID = -8581467028129479875L;

    /**
     * Creates new not found exception
     * 
     * @param error
     *            the error code
     * @param message
     *            the error message
     */
    public NotFoundException(Integer error, String message) {
        super(error, message);
    }

    /**
     * Creates new not found exception
     * 
     * @param error
     *            the error code
     * @param message
     *            the error message
     * @param moreInfo
     *            the additional information
     */
    public NotFoundException(Integer error, String message, String moreInfo) {
        super(error, message, moreInfo);
    }

    /**
     * Creates new not found exception
     * 
     * @param error
     *            the error code
     * @param message
     *            the error message
     * @param e
     *            the causing exception. Its message will be saved as additional
     *            information
     */
    public NotFoundException(Integer error, String message, Throwable e) {
        super(error, message, e);
    }

    /**
     * Creates new not found exception
     * 
     * @param error
     *            the error code
     * @param e
     *            the causing exception. Its message will be reused.
     */
    public NotFoundException(Integer error, Throwable e) {
        super(error, e);
    }
}
