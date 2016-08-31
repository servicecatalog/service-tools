/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 23, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.oscm.common.interfaces.exceptions.ComponentException;
import org.oscm.common.interfaces.exceptions.ConnectionException;

import com.google.gson.Gson;

/**
 * Super class for jms message producers
 * 
 * @author miethaner
 */
public class Producer {

    private Connection conn;
    private Session session;
    private MessageProducer producer;

    /**
     * Initializes the jms context
     * 
     * @param factory
     *            the connection factory
     * @param dest
     *            the destination of the connection
     */
    protected void init(ConnectionFactory factory, Destination dest) {

        if (factory != null && dest != null) {
            try {
                this.conn = factory.createConnection();
                this.session = conn.createSession(false,
                        Session.DUPS_OK_ACKNOWLEDGE);
                this.producer = session.createProducer(dest);
                this.producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            } catch (JMSException e) {
                // TODO log error

                conn = null;
                session = null;
                producer = null;
            }
        }
    }

    /**
     * Closes the jms context
     */
    protected void close() {
        if (conn != null) {
            try {
                producer.close();
                session.close();
                conn.close();
            } catch (JMSException e) {
                // TODO log but continue
            }
        }
    }

    /**
     * Sends a message with the given content to the corresponding queue or
     * topic. Throws a ConnectionException if the destination is somehow no
     * available.
     * 
     * @param content
     *            the content to send
     * @throws ComponentException
     */
    protected void produceMessage(Representation content)
            throws ComponentException {

        if (conn != null) {
            try {
                Gson gson = new Gson();
                String json = gson.toJson(content);

                TextMessage msg = session.createTextMessage();
                msg.setText(json);
                producer.send(msg);
            } catch (JMSException e) {
                throw new ConnectionException(new Integer(1), e);
                // TODO add error message
            }
        } else {
            throw new ConnectionException(new Integer(1), "");
            // TODO add error message
        }
    }
}
