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
    JERSEY_PORT("http-port", false, "8080"), //
    JERSEY_CONTEXT("http-context", false, "oscm"), //
    JERSEY_KEYSTORE_LOCATION("ssl-keystore-location", true, "./keystore.jks"), //
    JERSEY_KEYSTORE_PASSWORD("ssl-keystore-password", true, "changeit"), //
    JERSEY_TOKEN_SECRET("auth-token-secret", true, "secret"), //
    JERSEY_REQUEST_TIMEOUT("http-request-timeout", false, "60"); //

    private String name;
    private boolean mandatory;
    private String defaultValue;

    private JerseyConfig(String name, boolean mandatory, String defaultValue) {
        this.name = name;
        this.mandatory = mandatory;
        this.defaultValue = defaultValue;
    }

    @Override
    public String getConfigurationName() {
        return name;
    }

    @Override
    public String getProprietaryName() {
        return "";
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