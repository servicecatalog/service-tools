/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jan 18, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest;

import org.oscm.common.interfaces.keys.ConfigurationKey;

/**
 * Enum for configuration keys specific to jersey and rest.
 * 
 * @author miethaner
 */
public enum JerseyConfig implements ConfigurationKey {
    JERSEY_PORT("", false, "8080"), //
    JERSEY_CONTEXT("", false, "oscm"), //
    JERSEY_KEYSTORE_LOCATION("", true, "./keystore.jks"), //
    JERSEY_KEYSTORE_PASSWORD("", true, "changeit"), //
    JERSEY_TOKEN_SECRET("", true, "secret"), //
    JERSEY_REQUEST_TIMEOUT("", false, "180"); //

    private String proprietary;
    private boolean mandatory;
    private String defaultValue;

    private JerseyConfig(String proprietary, boolean mandatory,
            String defaultValue) {
        this.proprietary = proprietary;
        this.mandatory = mandatory;
        this.defaultValue = defaultValue;
    }

    @Override
    public String getConfigurationName() {
        return name();
    }

    @Override
    public String getProprietaryName() {
        return proprietary;
    }

    @Override
    public boolean isMandatory() {
        return mandatory;
    }

    @Override
    public String getDefaultValue() {
        return defaultValue;
    }
}