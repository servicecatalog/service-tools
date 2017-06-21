/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Dec 6, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.keys;

/**
 * Interface for enums that represent configuration keys.
 * 
 * @author miethaner
 */
public interface ConfigurationKey {

    /**
     * Gets the unique name identifying the configuration key.
     * 
     * @return the name
     */
    public String getConfigurationName();

    /**
     * Gets the name of the proprietary property this key is meant for.
     * 
     * @return the name
     */
    public String getProprietaryName();

    /**
     * Returns true if the setting is mandatory.
     * 
     * @return true if mandatory
     */
    public boolean isMandatory();

    /**
     * Gets the default value for the configuration key.
     * 
     * @return the default value
     */
    public String getDefaultValue();
}
