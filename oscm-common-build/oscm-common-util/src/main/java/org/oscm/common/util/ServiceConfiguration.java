/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Dec 6, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.oscm.common.interfaces.config.ConfigurationImporter;
import org.oscm.common.interfaces.config.ConfigurationKey;
import org.oscm.common.interfaces.config.ServiceKey;
import org.oscm.common.interfaces.config.VersionKey;

/**
 * Singleton class to manage configuration settings and service access roles.
 * 
 * @author miethaner
 */
public class ServiceConfiguration {

    private static ServiceConfiguration sc;

    /**
     * Returns the singleton instance of the service configuration.
     * 
     * @return the service configuration
     */
    public static ServiceConfiguration getInstance() {
        if (sc == null) {
            sc = new ServiceConfiguration();
        }

        return sc;
    }

    /**
     * Initializes the service configuration from the given importer and saves
     * the data corresponding to the given configuration and service keys. This
     * overwrites the previous service configuration instance.
     * 
     * @param importer
     *            the handler for the configuration source
     * @param services
     *            the service keys
     * @param configs
     *            the configuration keys
     */
    public static void init(ConfigurationImporter importer,
            VersionKey[] versions, ServiceKey[] services,
            ConfigurationKey[] configs) {
        sc = new ServiceConfiguration(importer, versions, services, configs);
    }

    private Map<ServiceKey, Set<String>> roles;
    private Map<ConfigurationKey, String> entries;
    private Set<VersionKey> versions;

    private ServiceConfiguration() {
        this.roles = Collections.emptyMap();
        this.entries = Collections.emptyMap();
    }

    private ServiceConfiguration(ConfigurationImporter importer,
            VersionKey[] versions, ServiceKey[] services,
            ConfigurationKey[] configs) {
        this.roles = new HashMap<>();
        this.entries = new HashMap<>();
        this.versions = new HashSet<>(Arrays.asList(versions));

        Map<String, Set<String>> tmpRoles = importer.readRoles();
        for (ServiceKey s : services) {
            if (tmpRoles.containsKey(s.getKeyName())
                    && tmpRoles.get(s.getKeyName()) != null
                    && !tmpRoles.get(s.getKeyName()).isEmpty()) {
                roles.put(s, tmpRoles.get(s.getKeyName()));
            } else {
                roles.put(s,
                        new HashSet<>(Arrays.asList(ServiceKey.PRIVATE_ROLE)));
            }
        }

        Map<String, String> tmpEntries = importer.readEntries();
        for (ConfigurationKey c : configs) {
            if (tmpEntries.containsKey(c.getKeyName())) {
                entries.put(c, tmpEntries.get(c.getKeyName()));
            } else {
                if (c.isMandatory()) {
                    throw new RuntimeException("Mandatory configuration entry "
                            + c.getKeyName() + " is missing");
                } else {
                    entries.put(c, c.getDefaultValue());
                }
            }
        }
    }

    public Set<VersionKey> getVersions() {
        return Collections.unmodifiableSet(versions);
    }

    /**
     * Gets the value for the given configuration key. Returns null if the key
     * does not exist.
     * 
     * @param config
     *            the configuration key
     * @return the configuration value
     */
    public String getConfig(ConfigurationKey config) {
        return entries.get(config);
    }

    /**
     * Gets an immutable map of values for the configuration keys with the
     * proprietary names as key.
     * 
     * @param configs
     *            the configuration keys
     * @return the immutable map
     */
    public Map<String, String> getProprietaryConfig(
            ConfigurationKey[] configs) {
        Map<String, String> map = new HashMap<>();

        for (ConfigurationKey key : configs) {
            map.put(key.getProprietaryName(), entries.get(key));
        }

        return Collections.unmodifiableMap(map);
    }

    /**
     * Gets an immutable map of all available configuration settings.
     * 
     * @return the immutable map
     */
    public Map<ConfigurationKey, String> getAllConfigs() {
        return Collections.unmodifiableMap(entries);
    }

    /**
     * Return true if the service of the given key is restricted.
     * 
     * @param service
     *            the service key
     * @return true if restricted
     */
    public boolean isServiceRestricted(ServiceKey service) {
        return !roles.get(service).contains(ServiceKey.PUBLIC_ROLE);
    }

    /**
     * Gets all roles for the service of the given key.
     * 
     * @param service
     *            the service key
     * @return the set of roles
     */
    public Set<String> getRolesForService(ServiceKey service) {
        return Collections.unmodifiableSet(roles.get(service));
    }
}
