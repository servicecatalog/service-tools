/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Dec 21, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.config;

/**
 * Interface for enums that represent error keys.
 * 
 * @author miethaner
 */
public interface ErrorKey {

    /**
     * Gets the name of the error key.
     * 
     * @return the error key name
     */
    public String getKeyName();

    /**
     * Gets the error id.
     * 
     * @return the error id.
     */
    public Integer getCode();

    /**
     * Gets the error message.
     * 
     * @param values
     *            the values for message placeholders
     * @return the error message
     */
    public String getMessage(String... values);
}
