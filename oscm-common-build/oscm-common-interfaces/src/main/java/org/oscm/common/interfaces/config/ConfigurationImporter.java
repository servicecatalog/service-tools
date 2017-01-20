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

/**
 * Interface for configuration importer classes.
 * 
 * @author miethaner
 */
public interface ConfigurationImporter {

    /**
     * Reads all key - set pairs for the service access roles from the source.
     * 
     * @return the map with the roles for services
     */
    public Map<String, Set<String>> readRoles();

    /**
     * Reads all configuration key - value pairs form the source.
     * 
     * @return the map with the configuration settings
     */
    public Map<String, String> readEntries();
}
