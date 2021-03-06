/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Dec 6, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.config;

import java.util.Map;
import java.util.Set;

import org.oscm.common.interfaces.keys.ActivityKey;
import org.oscm.common.interfaces.keys.ConfigurationKey;

/**
 * Interface for configuration importer classes.
 * 
 * @author miethaner
 */
public interface ConfigurationImporter {

    /**
     * Reads all access roles for the given service keys from the source.
     * 
     * @param keys
     *            the service keys to import
     * 
     * @return the map with the roles for services
     */
    public Map<ActivityKey, Set<String>> readRoles(ActivityKey[] keys);

    /**
     * Reads all values for the given configuration keys form the source.
     * 
     * @param keys
     *            the configuration keys to import
     * 
     * @return the map with the configuration settings
     */
    public Map<ConfigurationKey, String> readEntries(ConfigurationKey[] keys);
}
