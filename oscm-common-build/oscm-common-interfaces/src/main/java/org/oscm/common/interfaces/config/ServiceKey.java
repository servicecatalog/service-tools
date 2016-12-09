/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Dec 6, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.config;

/**
 * Interface for enums that represent service keys.
 * 
 * @author miethaner
 */
public interface ServiceKey {

    /**
     * No restrictions for a service with public role.
     */
    public static final String PUBLIC_ROLE = "PUBLIC";
    /**
     * Private role is used by services to make internal calls.
     */
    public static final String PRIVATE_ROLE = "PRIVATE";

    /**
     * Gets the name of the service key
     * 
     * @return the service key name
     */
    public String getKeyName();
}
