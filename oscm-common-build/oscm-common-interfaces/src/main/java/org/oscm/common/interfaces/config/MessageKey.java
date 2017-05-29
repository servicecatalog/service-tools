/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Dec 21, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.config;

/**
 * Interface for enums that represent message keys.
 * 
 * @author miethaner
 */
public interface MessageKey {

    /**
     * Gets the name of the message key.
     * 
     * @return the message key name
     */
    public String getKeyName();

    /**
     * Gets the message id.
     * 
     * @return the message id.
     */
    public Integer getCode();

    /**
     * Gets the corresponding message.
     * 
     * @param values
     *            the values to replace placeholders with
     * 
     * @return the message
     */
    public String getMessage(String... values);
}
