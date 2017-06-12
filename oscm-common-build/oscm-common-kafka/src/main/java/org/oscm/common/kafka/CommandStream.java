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
import org.oscm.common.interfaces.services.CommandService;
import org.oscm.common.kafka.mappers.CommandResultMapper;
import org.oscm.common.kafka.mappers.ResultEventMapper;
import org.oscm.common.util.ServiceConfiguration;

/**
 * @author miethaner
 *
 */
public class CommandStream extends Stream {

    private String commandTopic;
    private String resultTopic;
    private String eventTopic;
    private ActivityKey command;
    private CommandService service;

    public CommandStream(String commandTopic, String resultTopic,
            String eventTopic, ActivityKey command, CommandService service) {
        super();
        this.commandTopic = commandTopic;
        this.resultTopic = resultTopic;
        this.eventTopic = eventTopic;
        this.command = command;
        this.service = service;
    }

    @Override
    protected KafkaStreams initStream() {

        KStreamBuilder builder = new KStreamBuilder();

        KStream<UUID, Command> stream = builder.stream(new UUIDSerializer(),
                new DataSerializer<>(Command.class), commandTopic);

        stream.filter((key, value) -> value.getCommand().equals(command))
                .map(new CommandResultMapper(service)) //
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
        ServiceConfiguration.getInstance()
                .getProprietaryConfig(KafkaConfig.values())
                .forEach((key, value) -> config.put(key, value));

        return config;
    }
}
