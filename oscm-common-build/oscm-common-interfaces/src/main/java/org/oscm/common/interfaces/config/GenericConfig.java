/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 17, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.config;

import java.util.Map;
import java.util.Set;

/**
 * Interface for accessing configuration data.
 * 
 * @param E
 *            the type of the configuration key (usually a enum)
 * @param S
 *            the type of the service key (usually a enum)
 * 
 * @author miethaner
 */
public interface GenericConfig<E, S> {

    /**
     * Gets the value for the given configuration key. Returns null if not
     * present.
     * 
     * @param config
     *            the configuration key
     * @return the configuration value or null
     */
    public String getConfig(E config);

    /**
     * Gets all available configuration key-value pairs.
     * 
     * @return the map of configuration
     */
    public Map<E, String> getAllConfigs();

    /**
     * Returns true if the service with the given key is restricted to specific
     * roles.
     * 
     * @param service
     *            the service key
     * @return true if restricted
     */
    public boolean isServiceRestricted(S service);

    /**
     * Gets all possible roles for the service with the given key. Returns null
     * if the key does not exists or no roles are set.
     * 
     * @param service
     *            the service key
     * @return the set of roles or null
     */
    public Set<String> getRolesForService(S service);
}
