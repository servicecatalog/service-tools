/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Dec 19, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.kafka;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.oscm.common.interfaces.exceptions.ServiceException;

/**
 * Super class for threads that consume kafka records and hands them over to a
 * proxy listener.
 * 
 * @author miethaner
 */
public abstract class Consumer<R extends Representation> implements Runnable {

    public interface Handler<H extends Representation> {

        public void handleEvent(H rep) throws ServiceException;
    }

    private final AtomicBoolean closed = new AtomicBoolean(false);
    private Thread thread;

    private KafkaConsumer<Long, R> consumer;
    private Handler<R> handler;
    private Class<R> clazz;

    /**
     * Constructs new consumer thread for the given topic. Consumed records are
     * sent to the given listener. If reset is set, the read offset of the
     * consumer is set to the beginning to reread all records.
     * 
     * @param topic
     *            the topic to consume
     * @param listener
     *            the listener to handle the records
     * @param reset
     *            if true, reread all records
     */
    @SuppressWarnings("unchecked")
    public Consumer(String topic, Handler<R> handler) {
        this.handler = handler;

        this.clazz = (Class<R>) ((ParameterizedType) this.getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];

        this.consumer = ConnectionManager.getInstance().getConsumer(clazz);
        this.consumer.subscribe(Arrays.asList(topic));
    }

    @Override
    public final void run() {
        try {
            while (!closed.get()) {
                ConsumerRecords<Long, R> records = consumer
                        .poll(Long.MAX_VALUE);

                for (ConsumerRecord<Long, R> record : records) {
                    R rep = record.value();
                    rep.update();

                    try {
                        handler.handleEvent(rep);
                    } catch (ServiceException e) {
                        // TODO Log error
                    }
                }

                consumer.commitAsync();
            }
        } catch (WakeupException e) {
            // Ignore exception if closing
            if (!closed.get()) {
                throw e;
            }
        } finally {
            thread = null;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        consumer.close();
    }

    /**
     * Starts the consumer thread.
     */
    public final void startup() {
        if (thread != null) {
            thread = new Thread(this);
            closed.set(false);
            thread.start();
        }
    }

    /**
     * Resets the offset to read from the beginning if the thread is not
     * running.
     */
    public final void reset() {
        if (thread == null) {
            consumer.seekToBeginning(Collections.emptyList());
        }
    }

    /**
     * Stops the consumer thread.
     */
    public final void shutdown() {
        closed.set(true);
        consumer.wakeup();
    }
}
