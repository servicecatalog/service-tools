/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jan 18, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.hibernate;

import org.oscm.common.interfaces.config.ConfigurationKey;

/**
 * Enum for configuration keys specific to hibernate.
 * 
 * @author miethaner
 */
public enum HibernateConfig implements ConfigurationKey {
    DB_URL("javax.persistence.jdbc.url", true, "url", Type.URL), //
    DB_DRIVER("javax.persistence.jdbc.driver", true, "driver", Type.STRING), //
    DB_USER("javax.persistence.jdbc.user", true, "user", Type.STRING), //
    DB_PWD("javax.persistence.jdbc.password", true, "pwd", Type.PASSWORD), //
    DB_DIALECT("hibernate.dialect", true, "dialect", Type.STRING); //

    private String proprietary;
    private boolean mandatory;
    private String defaultValue;
    private Type valueType;

    private HibernateConfig(String proprietary, boolean mandatory,
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
