/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 9, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.kafka;

import org.apache.kafka.streams.KafkaStreams;
import org.oscm.common.interfaces.data.Version;
import org.oscm.common.interfaces.keys.ApplicationKey;
import org.oscm.common.interfaces.keys.EntityKey;
import org.oscm.common.util.ConfigurationManager;
import org.oscm.common.util.logger.ServiceLogger;

/**
 * Superclass for all kafka streams.
 * 
 * @author miethaner
 */
public abstract class Stream {

    private static final String APPLICATION_ID = "%s-%s-v%d";
    private static final String COMMAND_TOPIC = "%s-command";
    private static final String RESULT_TOPIC = "%s-result";
    private static final String EVENT_TOPIC = "%s-%s";

    private static final ServiceLogger LOGGER = ServiceLogger
            .getLogger(Stream.class);

    private KafkaStreams streams;

    /**
     * Initializes all streams for this context.
     * 
     * @return the streams
     */
    protected abstract KafkaStreams initStreams();

    /**
     * Builds the application id string for the kafka configuration from the
     * given identifier. The application id represents the context for this
     * stream.
     * 
     * @param identifier
     *            the method identifier
     * @return the id string
     */
    protected String buildApplicationId(String identifier) {
        ConfigurationManager cm = ConfigurationManager.getInstance();

        String prefix = cm.getSelf().getApplicationName();
        Version version = cm.getCurrentVersion();

        return String.format(APPLICATION_ID, prefix, identifier,
                new Integer(version.getCompiledVersion()));
    }

    /**
     * Builds the command topic string from the given application.
     * 
     * @param application
     *            the application key
     * @return the topic string
     */
    protected String buildCommandTopic(ApplicationKey application) {
        return String.format(COMMAND_TOPIC, application.getApplicationName());
    }

    /**
     * Builds the result topic string from the given application.
     * 
     * @param application
     *            the application key
     * @return the topic string
     */
    protected String buildResultTopic(ApplicationKey application) {
        return String.format(RESULT_TOPIC, application.getApplicationName());
    }

    /**
     * Builds the event topic string from the given entity.
     * 
     * @param entity
     *            the entity key
     * @return the topic string
     */
    protected String buildEventTopic(EntityKey entity) {
        return String.format(EVENT_TOPIC,
                entity.getApplication().getApplicationName(),
                entity.getEntityName());
    }

    /**
     * Initializes and starts the stream.
     */
    public void start() {
        streams = initStreams();
        streams.setUncaughtExceptionHandler((t, e) -> LOGGER.exception(e));

        streams.start();
    }

    /**
     * Stops the stream gracefully. Blocks until the shutdown is finished.
     */
    public void stop() {
        streams.close();
    }
}
