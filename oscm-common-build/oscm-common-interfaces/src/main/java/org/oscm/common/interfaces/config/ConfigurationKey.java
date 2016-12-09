/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Dec 6, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.config;

/**
 * Interface for enums that represent configuration keys.
 * 
 * @author miethaner
 */
public interface ConfigurationKey {

    /**
     * Enum for the value type of configuration settings
     * 
     * @author miethaner
     */
    public enum Type {
        BOOLEAN, LONG, STRING, PASSWORD, URL, MAIL;
    }

    /**
     * Gets the name of the configuration key
     * 
     * @return the configuration key name
     */
    public String getKeyName();

    /**
     * Returns true if the setting is mandatory
     * 
     * @return true if mandatory
     */
    public boolean isMandatory();

    /**
     * Gets the default value for the configuration key
     * 
     * @return the default value
     */
    public String getDefaultValue();

    /**
     * Gets the value type of the configuration value
     * 
     * @return the value type
     */
    public Type getValueType();

}
