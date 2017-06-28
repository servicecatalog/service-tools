/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jan 18, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.kafka;

import org.apache.kafka.streams.StreamsConfig;
import org.oscm.common.interfaces.keys.ConfigurationKey;

/**
 * Enum for configuration keys specific to kafka.
 * 
 * @author miethaner
 */
public enum KafkaConfig implements ConfigurationKey {
    KAFKA_SERVERS("kafka-servers", StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, true,
            "servers"); //

    private String name;
    private String proprietary;
    private boolean mandatory;
    private String defaultValue;

    private KafkaConfig(String name, String proprietary, boolean mandatory,
            String defaultValue) {
        this.name = name;
        this.proprietary = proprietary;
        this.mandatory = mandatory;
        this.defaultValue = defaultValue;
    }

    @Override
    public String getConfigurationName() {
        return name;
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
