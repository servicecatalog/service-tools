/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Dec 20, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.kafka;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.kafka.streams.state.Stores;
import org.oscm.common.interfaces.data.Command;
import org.oscm.common.interfaces.data.Result;
import org.oscm.common.interfaces.events.CommandPublisher;
import org.oscm.common.interfaces.events.ResultHandler;
import org.oscm.common.interfaces.exceptions.ServiceException;
import org.oscm.common.interfaces.keys.ApplicationKey;
import org.oscm.common.util.ConfigurationManager;

/**
 * Producer class for commands with kafka. Writes commands to a topic and waits
 * for corresponding results from another topic. The results will be given to
 * specified handlers.
 * 
 * @author miethaner
 */
public class CommandProducer extends Stream implements CommandPublisher {

    private static final String STORE_NAME = "result_store";
    private static final String SOURCE_NAME = "result_source";
    private static final String PROCESSOR_NAME = "result_processor";

    private class ResultProcessor implements Processor<UUID, Result> {

        @Override
        public void init(ProcessorContext context) {
        }

        @Override
        public void process(UUID key, Result value) {

            if (waitingHandlers.containsKey(key)) {
                waitingHandlers.get(key).handle(value);
                waitingHandlers.remove(key);
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
    private ApplicationKey application;
    private String commandTopic;

    private KafkaStreams localStreams;
    private ReadOnlyKeyValueStore<UUID, Result> store;

    /**
     * Creates a new kafka producer for commands of the given application that
     * will write to the command topic of that application. It also creates a
     * global stream from the result topic to monitor for corresponding results.
     * 
     * @param commandTopic
     *            the command topic
     * @param resultTopic
     *            the result topic
     * @param application
     *            the application key
     */
    public CommandProducer(ApplicationKey application) {
        super();
        this.application = application;
        this.commandTopic = buildCommandTopic(application);
        this.waitingHandlers = new ConcurrentHashMap<>();
    }

    @Override
    protected KafkaStreams initStreams() {

        UUIDSerializer keySerializer = new UUIDSerializer();
        DataSerializer<Command> commandSerializer = new DataSerializer<>(
                Command.class);
        DataSerializer<Result> resultSerializer = new DataSerializer<>(
                Result.class);

        producer = new KafkaProducer<>(getConfig(), keySerializer,
                commandSerializer);

        KStreamBuilder builder = new KStreamBuilder();

        StateStore resultStore = Stores.create(STORE_NAME)
                .withKeys(keySerializer) //
                .withValues(resultSerializer) //
                .persistent() //
                .disableLogging() //
                .build().get();

        builder.addGlobalStore(resultStore, SOURCE_NAME, keySerializer,
                resultSerializer, buildResultTopic(application), PROCESSOR_NAME,
                ResultProcessor::new);

        localStreams = new KafkaStreams(builder,
                new StreamsConfig(getConfig()));

        return localStreams;
    }

    private Map<String, Object> getConfig() {

        Map<String, Object> config = new HashMap<>();
        ConfigurationManager.getInstance()
                .getProprietaryConfig(KafkaConfig.values())
                .forEach((key, value) -> config.put(key, value));

        config.put(StreamsConfig.APPLICATION_ID_CONFIG,
                buildApplicationId(application.getApplicationName()));

        return config;
    }

    @Override
    public void publish(Command command, ResultHandler handler)
            throws ServiceException {

        waitingHandlers.put(command.getId(), handler);
        handler.onTimeout(() -> waitingHandlers.remove(command.getId()));

        producer.send(
                new ProducerRecord<>(commandTopic, command.getId(), command),
                (meta, e) -> {
                    if (e != null) {
                        waitingHandlers.remove(command.getId());
                        handler.handle(e);
                    }
                });
    }

    @Override
    public Result getResult(UUID id) {
        if (store == null) {
            store = localStreams.store(STORE_NAME,
                    QueryableStoreTypes.keyValueStore());
        }

        return store.get(id);
    }

    @Override
    public List<Result> getAllResults() {
        if (store == null) {
            store = localStreams.store(STORE_NAME,
                    QueryableStoreTypes.keyValueStore());
        }

        List<Result> list = new ArrayList<>();
        store.all().forEachRemaining((record) -> list.add(record.value));

        return list;
    }
}
