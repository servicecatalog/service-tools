/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Dec 20, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.kafka;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.StateStore;
import org.apache.kafka.streams.state.Stores;
import org.oscm.common.interfaces.data.Command;
import org.oscm.common.interfaces.data.Result;
import org.oscm.common.interfaces.events.CommandPublisher;
import org.oscm.common.interfaces.events.ResultHandler;
import org.oscm.common.interfaces.exceptions.ServiceException;
import org.oscm.common.interfaces.keys.ServiceKey;
import org.oscm.common.util.ConfigurationManager;

/**
 * Producer class for commands with kafka. Writes commands to a topic and waits
 * for corresponding results from another topic. The results will be given to
 * specified handlers.
 * 
 * @author miethaner
 */
public class CommandProducer extends Stream implements CommandPublisher {

    private static final String RESULT_STORE = "results";
    private static final String SOURCE_NAME = "result_source";
    private static final String PROCESSOR_NAME = "result_processor";

    private class ResultProcessor implements Processor<UUID, Result> {

        @Override
        public void init(ProcessorContext context) {
        }

        @Override
        public void process(UUID key, Result value) {

            if (value != null && value.getParent() != null
                    && waitingHandlers.containsKey(value.getParent())) {
                waitingHandlers.get(value.getParent()).handle(value);
                waitingHandlers.remove(value.getParent());
            }
        }

        @Override
        public void punctuate(long timestamp) {
        }

        @Override
        public void close() {
        }
    }

    private Map<UUID, ResultHandler> waitingHandlers;

    private KafkaProducer<UUID, Command> producer;
    private ServiceKey service;
    private String commandTopic;
    private String resultTopic;

    /**
     * Creates a new kafka producer for commands of the given service that will
     * write to the given command topic. It also creates a global stream from
     * the given result topic to monitor for corresponding results.
     * 
     * @param commandTopic
     *            the command topic
     * @param resultTopic
     *            the result topic
     * @param service
     *            the service key
     */
    public CommandProducer(String commandTopic, String resultTopic,
            ServiceKey service) {
        super();
        this.service = service;
        this.commandTopic = commandTopic;
        this.resultTopic = resultTopic;
        this.waitingHandlers = new ConcurrentHashMap<>();

        producer = new KafkaProducer<>(getConfig(), new UUIDSerializer(),
                new DataSerializer<>(Command.class));
    }

    @Override
    protected KafkaStreams initStreams() {
        KStreamBuilder builder = new KStreamBuilder();

        StateStore store = Stores.create(RESULT_STORE)
                .withKeys(new UUIDSerializer())
                .withValues(new DataSerializer<>(Result.class)).inMemory()
                .disableLogging().build().get();

        builder.addGlobalStore(store, SOURCE_NAME, new UUIDSerializer(),
                new DataSerializer<>(Result.class), resultTopic, PROCESSOR_NAME,
                ResultProcessor::new);

        KafkaStreams streams = new KafkaStreams(builder,
                new StreamsConfig(getConfig()));

        return streams;
    }

    private Map<String, Object> getConfig() {

        Map<String, Object> config = new HashMap<>();
        ConfigurationManager.getInstance()
                .getProprietaryConfig(KafkaConfig.values())
                .forEach((key, value) -> config.put(key, value));

        config.put(APPLICATION_ID,
                buildApplicationId("CMD_" + service.getServiceName()));

        return config;
    }

    @Override
    public void publish(Command command, ResultHandler handler)
            throws ServiceException {

        waitingHandlers.put(command.getId(), handler);
        handler.onTimeout(() -> waitingHandlers.remove(handler));

        producer.send(
                new ProducerRecord<>(commandTopic, command.getId(), command),
                (meta, e) -> {
                    if (e != null) {
                        waitingHandlers.remove(command.getId());
                        handler.handle(e);
                    }
                });
    }
}
