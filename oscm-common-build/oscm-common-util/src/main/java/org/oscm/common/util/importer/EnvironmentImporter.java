/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: May 10, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.util.importer;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.oscm.common.interfaces.config.ConfigurationImporter;
import org.oscm.common.interfaces.keys.ActivityKey;
import org.oscm.common.interfaces.keys.ConfigurationKey;

/**
 * Importer class for environment variables
 * 
 * @author miethaner
 */
public class EnvironmentImporter implements ConfigurationImporter {

    @Override
    public Map<ActivityKey, Set<String>> readRoles(ActivityKey[] keys) {

        Map<String, String> env = System.getenv();

        Map<ActivityKey, Set<String>> roles = new HashMap<>();
        for (ActivityKey k : keys) {
            if (env.containsKey(k.getActivityName())) {
                String value = env.get(k.getActivityName());
                roles.put(k, new HashSet<>(Arrays.asList(value.split(","))));
            } else {
                roles.put(k, Collections.emptySet());
            }
        }
        return roles;
    }

    @Override
    public Map<ConfigurationKey, String> readEntries(ConfigurationKey[] keys) {

        Map<String, String> env = System.getenv();

        Map<ConfigurationKey, String> entries = new HashMap<>();
        for (ConfigurationKey k : keys) {
            if (env.containsKey(k.getConfigurationName())) {
                entries.put(k, env.get(k.getConfigurationName()));
            } else {
                if (k.isMandatory()) {
                    throw new RuntimeException("Mandatory configuration entry "
                            + k.getConfigurationName() + " is missing");
                } else {
                    entries.put(k, k.getDefaultValue());
                }
            }
        }
        return entries;
    }

}
