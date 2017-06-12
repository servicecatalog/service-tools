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

    public Stream() {
        streams = initStream();
        streams.setUncaughtExceptionHandler((t, e) -> LOGGER.exception(e));
    }

    protected abstract KafkaStreams initStream();

    public void start() {
        streams.start();
    }

    public void stop() {
        streams.close();
    }
}
