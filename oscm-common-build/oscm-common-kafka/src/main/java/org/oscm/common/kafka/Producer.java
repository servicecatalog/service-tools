/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Dec 20, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.kafka;

import java.lang.reflect.ParameterizedType;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.oscm.common.interfaces.config.VersionKey;
import org.oscm.common.interfaces.exceptions.ServiceException;

/**
 * Super class for all publisher classes.
 * 
 * @author miethaner
 */
public abstract class Producer<R extends Representation> {

    private KafkaProducer<Long, R> producer;
    private Class<R> clazz;
    private String topic;
    private VersionKey version;

    @SuppressWarnings("unchecked")
    public Producer(String topic) {
        this.topic = topic;
        this.version = ConnectionManager.getInstance()
                .getCompatibilityVersion();
        this.clazz = (Class<R>) ((ParameterizedType) this.getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
        this.producer = ConnectionManager.getInstance().getProducer(clazz);
    }

    /**
     * Sends a new record serialized from the given representation to kafka. The
     * callback is triggered in a new thread as soon the record was sent.
     * 
     * @param rep
     *            the representation to send
     * @param callback
     *            the callback to trigger
     */
    public void send(R rep, org.oscm.common.interfaces.data.Callback callback) {

        rep.setVersion(new Integer(version.getCompiledVersion()));
        rep.convert();

        producer.send(new ProducerRecord<>(topic, rep.getId(), rep),
                new Callback() {

                    @Override
                    public void onCompletion(RecordMetadata metadata,
                            Exception exception) {
                        if (exception == null) {
                            try {
                                callback.callback();
                            } catch (ServiceException e) {
                                // TODO log error
                            }
                        } else {
                            // TODO log error
                        }
                    }
                });
    }
}
