/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 9, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.kafka;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.oscm.common.interfaces.data.Command;
import org.oscm.common.interfaces.data.Event;
import org.oscm.common.interfaces.data.Result;
import org.oscm.common.interfaces.enums.Status;
import org.oscm.common.interfaces.events.CommandPublisher;
import org.oscm.common.interfaces.exceptions.ServiceException;
import org.oscm.common.interfaces.keys.ActivityKey;
import org.oscm.common.interfaces.services.CommandService;
import org.oscm.common.util.ConfigurationManager;
import org.oscm.common.util.ServiceManager;
import org.oscm.common.util.logger.ServiceLogger;

/**
 * Stream class for command pipelines with kafka. Takes a command from a topic
 * and processes it within a mapper. The result written back to a topic and also
 * mapped to an event that is written back separately.
 * 
 * @author miethaner
 */
public class CommandStream extends Stream {

    private static final ServiceLogger LOGGER = ServiceLogger
            .getLogger(CommandStream.class);

    private class CommandWrapper implements
            KeyValueMapper<UUID, Command, List<KeyValue<UUID, Result>>> {

        @Override
        public List<KeyValue<UUID, Result>> apply(UUID key, Command value) {

            ServiceManager sm = ServiceManager.getInstance();
            CommandService service = sm.getCommandService(command);
            CommandPublisher publisher = sm
                    .getPublisher(ConfigurationManager.getInstance().getSelf());

            if (value == null || !command.equals(value.getCommand())
                    || publisher.getResult(key) != null) {
                return new ArrayList<>();
            }

            Result result = new Result();
            result.setId(key);
            result.setCommand(value.getCommand());

            try {
                List<Event> events = service.execute(value);

                result.setEvents(events);
                result.setStatus(Status.SUCCESS);
            } catch (ServiceException e) {
                LOGGER.error(e);
                result.setStatus(Status.FAILED);
                result.setFailure(e.getAsFailure());
            }

            result.setTimestamp(new Date());

            return Arrays.asList(KeyValue.pair(result.getId(), result));
        }
    }

    private class ResultMapper implements
            KeyValueMapper<UUID, Result, List<KeyValue<UUID, Event>>> {

        @Override
        public List<KeyValue<UUID, Event>> apply(UUID key, Result value) {

            List<KeyValue<UUID, Event>> events = new ArrayList<>();

            if (value != null && command.equals(value.getCommand())
                    && value.getStatus() == Status.SUCCESS
                    && value.getEvents() != null) {

                value.getEvents().forEach(
                        (e) -> events.add(KeyValue.pair(e.getId(), e)));
            }

            return events;
        }

    }

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

        UUIDSerializer keySerializer = new UUIDSerializer();
        DataSerializer<Command> commandSerializer = new DataSerializer<>(
                Command.class);
        DataSerializer<Result> resultSerializer = new DataSerializer<>(
                Result.class);
        DataSerializer<Event> outputSerializer = new DataSerializer<>(
                command.getOutputEntity().getEntityClass());

        KStreamBuilder builder = new KStreamBuilder();

        builder.stream(keySerializer, commandSerializer,
                buildCommandTopic(command.getApplication())) //
                .flatMap(new CommandWrapper()) //
                .through(keySerializer, resultSerializer,
                        buildResultTopic(command.getApplication())) //
                .flatMap(new ResultMapper()) //
                .to(keySerializer, outputSerializer,
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
