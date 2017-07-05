/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 9, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.kafka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.oscm.common.interfaces.data.Event;
import org.oscm.common.interfaces.exceptions.ServiceException;
import org.oscm.common.interfaces.keys.TransitionKey;
import org.oscm.common.interfaces.services.TransitionService;
import org.oscm.common.util.ConfigurationManager;
import org.oscm.common.util.ServiceManager;
import org.oscm.common.util.logger.ServiceLogger;

/**
 * Stream class for scheduled transition pipelines with kafka. Takes a event
 * from a topic and adds it to scheduler that processes it periodically. The
 * resulting events are written back to another topic.
 * 
 * @author miethaner
 */
public class TimedStream extends Stream {

    private static final int POOL_SIZE = 4;
    private static final String SOURCE_NAME = "event_source";
    private static final String PROCESSOR_NAME = "event_processor";
    private static final String SINK_NAME = "event_source";

    private static final ServiceLogger LOGGER = ServiceLogger
            .getLogger(TimedStream.class);

    private class TimedProcessor implements Processor<UUID, Event> {

        private ScheduledExecutorService executor;
        private ProcessorContext context;
        private Map<UUID, ScheduledFuture<?>> tasks;

        @Override
        public void init(ProcessorContext context) {
            this.executor = Executors.newScheduledThreadPool(POOL_SIZE);
            this.context = context;
            this.tasks = new HashMap<>();
        }

        @Override
        public void process(UUID key, Event value) {

            if (tasks.containsKey(key)) {
                tasks.get(key).cancel(false);
            }

            Runnable r = () -> {
                try {
                    TransitionService service = ServiceManager.getInstance()
                            .getTransitionService(transition);
                    List<Event> events = service.process(value);

                    if (events == null) {
                        throw new RuntimeException("Remove Task");
                    }

                    events.forEach((e) -> context.forward(e.getId(), e));
                } catch (ServiceException e) {
                    LOGGER.error(e);
                }

            };

            ScheduledFuture<?> future = executor.scheduleWithFixedDelay(r, 0,
                    interval, TimeUnit.MILLISECONDS);

            tasks.put(key, future);
        }

        @Override
        public void punctuate(long timestamp) {
        }

        @Override
        public void close() {
        }
    }

    private TransitionKey transition;
    private long interval;

    /**
     * Creates a new kafka stream for the given transition key. The stream reads
     * from the input topic defined by the transition and add adds tasks per
     * event to a scheduler. The tasks calls the corresponding service with its
     * event. The resulting events are written to the output topic.
     * 
     * @param transition
     *            the transition key
     * @param interval
     *            the delay between task execution in milliseconds
     */
    public TimedStream(TransitionKey transition, long interval) {

        if (!transition.getOutputEntity().getApplication()
                .equals(ConfigurationManager.getInstance().getSelf())) {
            throw new RuntimeException(
                    "An output entity outside of this application is not allowed");
        }

        this.transition = transition;
        this.interval = interval;
    }

    @Override
    protected KafkaStreams initStreams() {

        UUIDSerializer keySerializer = new UUIDSerializer();
        DataSerializer<Event> inputSerializer = new DataSerializer<>(
                transition.getInputEntity().getEntityClass());
        DataSerializer<Event> outputSerializer = new DataSerializer<>(
                transition.getOutputEntity().getEntityClass());

        KStreamBuilder builder = new KStreamBuilder();

        builder.addSource(SOURCE_NAME, keySerializer, inputSerializer,
                buildEventTopic(transition.getInputEntity()));

        builder.addProcessor(PROCESSOR_NAME, TimedProcessor::new, SOURCE_NAME);

        builder.addSink(SINK_NAME,
                buildEventTopic(transition.getOutputEntity()), keySerializer,
                outputSerializer, PROCESSOR_NAME);

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
                buildApplicationId(transition.getTransitionName()));

        return config;
    }
}
