/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 9, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.kafka;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.oscm.common.interfaces.data.Command;
import org.oscm.common.interfaces.data.Result;
import org.oscm.common.interfaces.keys.ActivityKey;
import org.oscm.common.kafka.mappers.CommandResultMapper;
import org.oscm.common.kafka.mappers.ResultEventMapper;
import org.oscm.common.util.ConfigurationManager;

/**
 * Stream class for command pipelines with kafka. Takes a command from a topic
 * and processes it within a mapper. The result written back to a topic and also
 * mapped to an event that is written back separately.
 * 
 * @author miethaner
 */
public class CommandStream extends Stream {

    private ActivityKey command;

    /**
     * Creates new kafka stream for the given command key. The stream reads from
     * the command topic of this service and processes all matching commands
     * with the corresponding service. The result is written back to the result
     * topic. It is also mapped to an event written to the corresponding event
     * topic.
     * 
     * @param command
     *            the command key
     */
    public CommandStream(ActivityKey command) {

        if (!command.getOutputEntity().getApplication()
                .equals(ConfigurationManager.getInstance().getSelf())) {
            throw new RuntimeException(
                    "An output entity outside of this application is not allowed");
        }

        this.command = command;
    }

    @Override
    protected KafkaStreams initStreams() {

        KStreamBuilder builder = new KStreamBuilder();

        KStream<UUID, Command> stream = builder.stream(new UUIDSerializer(),
                new DataSerializer<>(Command.class),
                buildCommandTopic(command.getApplication()));

        stream.flatMap(new CommandResultMapper(command)) //
                .through(new UUIDSerializer(),
                        new DataSerializer<>(Result.class),
                        buildResultTopic(command.getApplication())) //
                .flatMap(new ResultEventMapper(command)) //
                .to(new UUIDSerializer(),
                        new DataSerializer<>(
                                command.getOutputEntity().getEntityClass()),
                        buildEventTopic(command.getOutputEntity()));

        KafkaStreams streams = new KafkaStreams(builder,
                new StreamsConfig(getConfig()));

        return streams;
    }

    private Map<String, Object> getConfig() {

        Map<String, Object> config = new HashMap<>();
        ConfigurationManager.getInstance()
                .getProprietaryConfig(KafkaConfig.values())
                .forEach((key, value) -> config.put(key, value));

        config.put(StreamsConfig.APPLICATION_ID_CONFIG,
                buildApplicationId(command.getActivityName()));

        return config;
    }
}
