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
 * @author miethaner
 *
 */
public class CommandStream extends Stream {

    private String commandTopic;
    private String resultTopic;
    private String eventTopic;
    private ActivityKey command;

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
                .flatMap(new ResultEventMapper()) //
                .through(new UUIDSerializer(),
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

        return config;
    }
}
