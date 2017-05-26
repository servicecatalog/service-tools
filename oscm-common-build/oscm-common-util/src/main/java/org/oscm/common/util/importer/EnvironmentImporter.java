/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: May 10, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.util.importer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.oscm.common.interfaces.config.ConfigurationImporter;
import org.oscm.common.interfaces.config.ConfigurationKey;
import org.oscm.common.interfaces.config.ResourceKey;

/**
 * Importer class for environment variables
 * 
 * @author miethaner
 */
public class EnvironmentImporter implements ConfigurationImporter {

    @Override
    public Map<ResourceKey, Set<String>> readRoles(ResourceKey[] keys) {

        Map<String, String> env = System.getenv();

        Map<ResourceKey, Set<String>> roles = new HashMap<>();
        for (ResourceKey k : keys) {
            if (env.containsKey(k.getKeyName())) {
                String value = env.get(k.getKeyName());
                roles.put(k, new HashSet<>(Arrays.asList(value.split(","))));
            } else {
                roles.put(k, new HashSet<>(Arrays.asList(k.getDefaultRole())));
            }
        }
        return roles;
    }

    @Override
    public Map<ConfigurationKey, String> readEntries(ConfigurationKey[] keys) {

        Map<String, String> env = System.getenv();

        Map<ConfigurationKey, String> entries = new HashMap<>();
        for (ConfigurationKey k : keys) {
            if (env.containsKey(k.getKeyName())) {
                entries.put(k, env.get(k.getKeyName()));
            } else {
                if (k.isMandatory()) {
                    throw new RuntimeException("Mandatory configuration entry "
                            + k.getKeyName() + " is missing");
                } else {
                    entries.put(k, k.getDefaultValue());
                }
            }
        }
        return entries;
    }

}
