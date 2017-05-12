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
import org.oscm.common.interfaces.config.ConfigurationKey;
import org.oscm.common.interfaces.config.ConfigurationLoader;
import org.oscm.common.interfaces.config.ServiceKey;

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
    public Map<ServiceKey, Set<String>> readRoles(ServiceKey[] keys) {
        try {
            Properties p = new Properties();
            p.load(loader.loadConfiguration());

            Map<ServiceKey, Set<String>> roles = new HashMap<>();
            for (ServiceKey k : keys) {
                if (p.containsKey(k.getKeyName())) {
                    String value = p.getProperty(k.getKeyName());
                    roles.put(k,
                            new HashSet<>(Arrays.asList(value.split(","))));
                } else {
                    roles.put(k,
                            new HashSet<>(Arrays.asList(k.getDefaultRole())));
                }
            }
            return roles;
        } catch (IOException e) {
            throw new RuntimeException(
                    "Unable to read roles from configuration");
        }
    }

    @Override
    public Map<ConfigurationKey, String> readEntries(ConfigurationKey[] keys) {
        try {
            Properties p = new Properties();
            p.load(loader.loadConfiguration());

            Map<ConfigurationKey, String> entries = new HashMap<>();
            for (ConfigurationKey k : keys) {
                if (p.containsKey(k.getKeyName())) {
                    entries.put(k, p.getProperty(k.getKeyName()));
                } else {
                    if (k.isMandatory()) {
                        throw new RuntimeException(
                                "Mandatory configuration entry "
                                        + k.getKeyName() + " is missing");
                    } else {
                        entries.put(k, k.getDefaultValue());
                    }
                }
            }
            return entries;
        } catch (IOException e) {
            throw new RuntimeException(
                    "Unable to read entries from configuration");
        }
    }

}
