/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jan 19, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.util.importer;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.oscm.common.interfaces.config.ConfigurationImporter;
import org.oscm.common.interfaces.config.ConfigurationLoader;

/**
 * Importer class for property sources.
 * 
 * @author miethaner
 */
public class PropertiesImporter implements ConfigurationImporter {

    private ConfigurationLoader loader;

    public PropertiesImporter(ConfigurationLoader loader) {
        this.loader = loader;
    }

    @Override
    public Map<String, Set<String>> readRoles() {
        try {
            Properties p = new Properties();
            p.load(loader.loadConfiguration());

            Map<String, Set<String>> roles = new HashMap<>();
            for (Object key : p.keySet()) {
                String value = p.getProperty((String) key);
                roles.put((String) key,
                        new HashSet<>(Arrays.asList(value.split(","))));
            }
            return roles;
        } catch (IOException e) {
            throw new RuntimeException(
                    "Unable to read roles from configuration");
        }
    }

    @Override
    public Map<String, String> readEntries() {
        try {
            Properties p = new Properties();
            p.load(loader.loadConfiguration());

            Map<String, String> entries = new HashMap<>();
            for (Object key : p.keySet()) {
                entries.put((String) key, p.getProperty((String) key));
            }
            return entries;
        } catch (IOException e) {
            throw new RuntimeException(
                    "Unable to read entries from configuration");
        }
    }

}
