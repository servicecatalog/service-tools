/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jun 24, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.exceptions;

/**
 * Component exception for conflict events and errors (e.g. violation of a
 * constraint)
 * 
 * @author miethaner
 */
public class ConflictException extends ComponentException {

    private static final long serialVersionUID = 5446563151469012266L;

    /**
     * Creates new conflict exception
     * 
     * @param error
     *            the error code
     * @param message
     *            the error message
     */
    public ConflictException(Integer error, String message) {
        super(error, message);
    }

    /**
     * Creates new conflict exception
     * 
     * @param error
     *            the error code
     * @param message
     *            the error message
     * @param moreInfo
     *            the additional information
     */
    public ConflictException(Integer error, String message, String moreInfo) {
        super(error, message, moreInfo);
    }

    /**
     * Creates new conflict exception
     * 
     * @param error
     *            the error code
     * @param message
     *            the error message
     * @param e
     *            the causing exception. Its message will be saved as additional
     *            information
     */
    public ConflictException(Integer error, String message, Throwable e) {
        super(error, message, e);
    }

    /**
     * Creates new conflict exception
     * 
     * @param error
     *            the error code
     * @param e
     *            the causing exception. Its message will be reused.
     */
    public ConflictException(Integer error, Throwable e) {
        super(error, e);
    }
}
