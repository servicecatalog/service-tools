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
import org.oscm.common.interfaces.enums.Status;
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

    private String commandTopic;
    private String resultTopic;
    private String eventTopic;
    private ActivityKey command;

    /**
     * Creates new kafka stream from the given command topic that is filtered
     * for the given command. After processing with the service identified by
     * the given command, the result is written back to the given result topic.
     * It is also mapped to an event written to the given event topic.
     * 
     * @param commandTopic
     *            the command topic
     * @param resultTopic
     *            the result topic
     * @param eventTopic
     *            the event topic
     * @param command
     *            the command key
     */
    public CommandStream(String commandTopic, String resultTopic,
            String eventTopic, ActivityKey command) {
        super();
        this.commandTopic = commandTopic;
        this.resultTopic = resultTopic;
        this.eventTopic = eventTopic;
        this.command = command;
    }

    @Override
    protected KafkaStreams initStreams() {

        KStreamBuilder builder = new KStreamBuilder();

        KStream<UUID, Command> stream = builder.stream(new UUIDSerializer(),
                new DataSerializer<>(Command.class), commandTopic);

        stream.filter((key, value) -> value.getCommand().equals(command))
                .map(new CommandResultMapper()) //
                .through(new UUIDSerializer(),
                        new DataSerializer<>(Result.class), resultTopic) //
                .filter((key, value) -> value.getCommand().equals(command)
                        && value.getStatus() == Status.SUCCESS
                        && value.getEvents() != null)
                .flatMap(new ResultEventMapper()) //
                .to(new UUIDSerializer(),
                        new DataSerializer<>(command.getOutputClass()),
                        eventTopic);

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
