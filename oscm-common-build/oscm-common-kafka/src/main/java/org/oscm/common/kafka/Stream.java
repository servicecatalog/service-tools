/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 9, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.kafka;

import org.apache.kafka.streams.KafkaStreams;
import org.oscm.common.util.logger.ServiceLogger;

/**
 * @author miethaner
 *
 */
public abstract class Stream {

    private static final ServiceLogger LOGGER = ServiceLogger
            .getLogger(Stream.class);

    private KafkaStreams streams;

    protected abstract KafkaStreams initStreams();

    public void start() {
        streams = initStreams();
        streams.setUncaughtExceptionHandler((t, e) -> LOGGER.exception(e));

        streams.start();
    }

    public void stop() {
        streams.close();
    }
}
