/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 9, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.kafka;

import org.apache.kafka.streams.KafkaStreams;
import org.oscm.common.interfaces.keys.VersionKey;
import org.oscm.common.util.ConfigurationManager;
import org.oscm.common.util.logger.ServiceLogger;

/**
 * @author miethaner
 *
 */
public abstract class Stream {

    public static final String APPLICATION_ID = "application.id";

    private static final ServiceLogger LOGGER = ServiceLogger
            .getLogger(Stream.class);

    private KafkaStreams streams;

    protected abstract KafkaStreams initStreams();

    public String buildApplicationId(String method) {
        ConfigurationManager cm = ConfigurationManager.getInstance();

        String prefix = cm.getConfig(KafkaConfig.KAFKA_APPLICATION_PREFIX);
        VersionKey version = cm.getCurrentVersion();

        return String.format("%s-%s-v%d", prefix, method,
                new Integer(version.getCompiledVersion()));
    }

    public void start() {
        streams = initStreams();
        streams.setUncaughtExceptionHandler((t, e) -> LOGGER.exception(e));

        streams.start();
    }

    public void stop() {
        streams.close();
    }
}
