/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jun 22, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.exceptions;

/**
 * Super class for all component specific exceptions
 * 
 * @author miethaner
 */
public class ComponentException extends Exception {

    private static final long serialVersionUID = -4770733030290897811L;

    private Integer error;
    private String moreInfo;

    /**
     * Creates new component exception
     * 
     * @param error
     *            the error code
     * @param message
     *            the error message
     */
    public ComponentException(Integer error, String message) {
        super(message);
        this.error = error;
    }

    /**
     * Creates new component exception
     * 
     * @param error
     *            the error code
     * @param e
     *            the causing exception. Its message will be reused.
     */
    public ComponentException(Integer error, Throwable e) {
        this(error, e.getMessage());
    }

    /**
     * Creates new component exception
     * 
     * @param error
     *            the error code
     * @param message
     *            the error message
     * @param moreInfo
     *            the additional information
     */
    public ComponentException(Integer error, String message, String moreInfo) {
        this(error, message);
        this.moreInfo = moreInfo;
    }

    /**
     * Creates new component exception
     * 
     * @param error
     *            the error code
     * @param message
     *            the error message
     * @param e
     *            the causing exception. Its message will be saved as additional
     *            information
     */
    public ComponentException(Integer error, String message, Throwable e) {
        this(error, message, e.getMessage());
    }

    /**
     * Gets the error code. Returns null if not set.
     * 
     * @return the error code or null
     */
    public Integer getError() {
        return error;
    }

    /**
     * Sets the error code.
     * 
     * @param error
     *            the error code. Can be null.
     */
    public void setError(Integer error) {
        this.error = error;
    }

    /**
     * Gets the additional information. Returns null if not present.
     * 
     * @return the additional information
     */
    public String getMoreInfo() {
        return moreInfo;
    }

    /**
     * Sets the additional information
     * 
     * @param moreInfo
     *            the additional information. Can be null
     */
    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }
}
