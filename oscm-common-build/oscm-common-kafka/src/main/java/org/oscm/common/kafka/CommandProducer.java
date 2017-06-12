/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Dec 20, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.kafka;

import java.util.HashMap;
import java.util.Iterator;
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
import org.oscm.common.util.ServiceConfiguration;

/**
 * 
 * @author miethaner
 */
public class CommandProducer extends Stream implements CommandPublisher {

    private static final String RESULT_STORE = "results";
    private static final String SOURCE_NAME = "result_source";
    private static final String PROCESSOR_NAME = "result_processor";
    private static final long PROCESSOR_INTERVAL = 1000; // ms?

    private class ResultProcessor implements Processor<UUID, Result> {

        @Override
        public void init(ProcessorContext context) {
            context.schedule(PROCESSOR_INTERVAL);
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
            Iterator<ResultHandler> it = waitingHandlers.values().iterator();

            while (it.hasNext()) {
                if (!it.next().isAlive()) {
                    it.remove();
                }
            }
        }

        @Override
        public void close() {
        }
    }

    private Map<UUID, ResultHandler> waitingHandlers;

    private KafkaProducer<UUID, Command> producer;
    private String commandTopic;
    private String resultTopic;

    public CommandProducer(String commandTopic, String resultTopic) {
        super();
        this.commandTopic = commandTopic;
        this.resultTopic = resultTopic;
        this.waitingHandlers = new ConcurrentHashMap<>();

        producer = new KafkaProducer<>(getConfig());
    }

    @Override
    protected KafkaStreams initStream() {
        KStreamBuilder builder = new KStreamBuilder();

        StateStore store = Stores.create(RESULT_STORE).withKeys(UUID.class)
                .withValues(Result.class).inMemory().build().get();

        builder.addGlobalStore(store, SOURCE_NAME, new UUIDSerializer(),
                new DataSerializer<>(Result.class), resultTopic, PROCESSOR_NAME,
                ResultProcessor::new);

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

    @Override
    public void publish(Command command, ResultHandler handler)
            throws ServiceException {

        waitingHandlers.put(command.getId(), handler);

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
