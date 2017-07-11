/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Dec 6, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.oscm.common.interfaces.config.ConfigurationImporter;
import org.oscm.common.interfaces.data.Version;
import org.oscm.common.interfaces.keys.ActivityKey;
import org.oscm.common.interfaces.keys.ApplicationKey;
import org.oscm.common.interfaces.keys.ConfigurationKey;

/**
 * Singleton class to manage configuration settings and resource access roles.
 * 
 * @author miethaner
 */
public class ConfigurationManager {

    public static final Charset CHARSET = StandardCharsets.UTF_8;
    public static final String FORMAT_DATE = "yyyy-MM-dd'T'HH:mm:ssXXX";

    private static volatile ConfigurationManager cm;

    /**
     * Returns the singleton instance of the configuration manager.
     * 
     * @return the service configuration
     */
    public static ConfigurationManager getInstance() {
        if (cm == null) {
            cm = new ConfigurationManager();
        }

        return cm;
    }

    /**
     * Initializes the service configuration from the given importer and saves
     * the data corresponding to the given configuration and activity keys. This
     * overwrites the previous service configuration instance.
     * 
     * @param importer
     *            the handler for the configuration source
     * @param activities
     *            the activity keys
     * @param configs
     *            the configuration keys
     */
    public static void init(ConfigurationImporter importer, ApplicationKey self,
            ActivityKey[] activities, Version current, Version compatible,
            ConfigurationKey[] configs) {
        cm = new ConfigurationManager(importer, self, activities, current,
                compatible, configs);
    }

    private Map<ActivityKey, Set<String>> roles;
    private Map<ConfigurationKey, String> entries;
    private Map<String, ActivityKey> activities;
    private ApplicationKey self;
    private Version current;
    private Version compatible;

    private ConfigurationManager() {
        roles = Collections.emptyMap();
        entries = Collections.emptyMap();
        activities = Collections.emptyMap();
        self = null;
        current = null;
        compatible = null;
    }

    private ConfigurationManager(ConfigurationImporter importer,
            ApplicationKey self, ActivityKey[] activities, Version current,
            Version compatible, ConfigurationKey[] configs) {

        this.self = self;

        this.activities = new HashMap<>();
        Arrays.asList(activities)
                .forEach((a) -> this.activities.put(a.getActivityName(), a));

        if (current.compare(compatible) < 0) {
            throw new RuntimeException(
                    "Current version number is lower than compatible version");
        }

        this.current = current;
        this.compatible = compatible;

        this.roles = importer.readRoles(activities);
        this.entries = importer.readEntries(configs);
    }

    /**
     * Gets the application key for this application.
     * 
     * @return the application
     */
    public ApplicationKey getSelf() {
        return self;
    }

    /**
     * Gets the current version key for this service.
     * 
     * @return the version
     */
    public Version getCurrentVersion() {
        return current;
    }

    /**
     * Gets the oldest compatible version key for this service.
     * 
     * @return the version
     */
    public Version getCompatibleVersion() {
        return compatible;
    }

    /**
     * Gets the activity with the given name for this service.
     * 
     * @param keyName
     *            the activity name
     * @return the activity
     */
    public ActivityKey getActivityForName(String keyName) {
        return activities.get(keyName);
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
     * Gets the value for the given configuration key as Boolean. Returns null
     * if the key does not exist.
     * 
     * @param config
     *            the configuration key
     * @return the configuration value
     */
    public Boolean getConfigAsBoolean(ConfigurationKey config) {
        if (entries.containsKey(config)) {
            return Boolean.valueOf(entries.get(config));
        } else {
            return null;
        }
    }

    /**
     * Gets the value for the given configuration key as Long. Returns null if
     * the key does not exist or is not a number.
     * 
     * @param config
     *            the configuration key
     * @return the configuration value
     */
    public Long getConfigAsLong(ConfigurationKey config) {
        if (entries.containsKey(config)) {
            try {
                return Long.valueOf(entries.get(config));
            } catch (NumberFormatException e) {
                return null;
            }
        } else {
            return null;
        }
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
            if (key.getProprietaryName() != null
                    && key.getProprietaryName().length() > 0) {
                map.put(key.getProprietaryName(), entries.get(key));
            }
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
     * Gets all roles for the activity of the given key.
     * 
     * @param activity
     *            the activity key
     * @return the set of roles
     */
    public Set<String> getRolesForActivity(ActivityKey activity) {
        if (roles.get(activity) != null) {
            return Collections.unmodifiableSet(roles.get(activity));
        } else {
            return Collections.emptySet();
        }
    }
}
