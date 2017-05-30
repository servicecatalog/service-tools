/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jan 18, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest;

import org.oscm.common.interfaces.config.ConfigurationKey;

/**
 * Enum for configuration keys specific to jersey and rest.
 * 
 * @author miethaner
 */
public enum JerseyConfig implements ConfigurationKey {
    JERSEY_PORT("", false, "8080", Type.LONG), //
    JERSEY_CONTEXT("", false, "oscm", Type.STRING), //
    JERSEY_KEYSTORE_LOCATION("", true, "./keystore.jks", Type.STRING), //
    JERSEY_KEYSTORE_PASSWORD("", true, "changeit", Type.STRING), //
    JERSEY_TOKEN_SECRET("", true, "secret", Type.STRING); //

    private String proprietary;
    private boolean mandatory;
    private String defaultValue;
    private Type valueType;

    private JerseyConfig(String proprietary, boolean mandatory,
            String defaultValue, Type valueType) {
        this.proprietary = proprietary;
        this.mandatory = mandatory;
        this.defaultValue = defaultValue;
        this.valueType = valueType;
    }

    @Override
    public String getKeyName() {
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

    @Override
    public Type getValueType() {
        return valueType;
    }
}