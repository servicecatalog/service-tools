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
    JERSEY_PORT("HTTP_PORT", false, "8080"), //
    JERSEY_CONTEXT("HTTP_CONTEXT", false, "oscm"), //
    JERSEY_KEYSTORE_LOCATION("SSL_KEYSTORE_LOCATION", false, "./keystore.jks"), //
    JERSEY_KEYSTORE_PASSWORD("SSL_KEYSTORE_PASSWORD", false, "changeit"), //
    JERSEY_TRUSTSTORE_LOCATION("SSL_TRUSTSTORE_LOCATION", false,
            "/opt/java/lib/security/cacerts"), //
    JERSEY_TRUSTSTORE_PASSWORD("SSL_TRUSTSTORE_PASSWORD", false, "changeit"), //
    JERSEY_TOKEN_SECRET("AUTH_TOKEN_SECRET", false, "secret"), //
    JERSEY_REQUEST_TIMEOUT("HTTP_REQUEST_TIMEOUT", false, "60"); //

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