/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Dec 6, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.config;

/**
 * Interface for enums that represent resource keys.
 * 
 * @author miethaner
 */
public interface ResourceKey {

    /**
     * No restrictions for a resource with public role.
     */
    public static final String PUBLIC_ROLE = "PUBLIC";
    /**
     * Private role is used by services to make internal calls.
     */
    public static final String PRIVATE_ROLE = "PRIVATE";

    /**
     * Gets the name of the resource key
     * 
     * @return the resource key name
     */
    public String getKeyName();

    /**
     * Gets the default role for the resource
     * 
     * @return the resource default role
     */
    public String getDefaultRole();
}
