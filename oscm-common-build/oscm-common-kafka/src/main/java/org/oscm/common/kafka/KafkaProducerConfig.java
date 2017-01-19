/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jan 18, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.kafka;

import org.oscm.common.interfaces.config.ConfigurationKey;

/**
 * Enum for configuration keys specific to kafka producers.
 * 
 * @author miethaner
 */
public enum KafkaProducerConfig implements ConfigurationKey {
    KAFKA_SERVERS("bootstrap.servers", true, "servers", Type.STRING); //

    private String proprietary;
    private boolean mandatory;
    private String defaultValue;
    private Type valueType;

    private KafkaProducerConfig(String proprietary, boolean mandatory,
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
