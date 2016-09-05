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
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.oscm.common.interfaces.exceptions.ComponentException;
import org.oscm.common.interfaces.exceptions.ConnectionException;
import org.oscm.common.interfaces.exceptions.ValidationException;
import org.oscm.common.interfaces.services.GenericReceiver;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Super class for jms listeners
 * 
 * @author miethaner
 */
public class Listener {

    /**
     * Takes the given message, converts the content into a object of the given
     * class and sends it to persistence. Errors will be logged but not
     * redirected.
     * 
     * @param msg
     *            the message to consume
     * @param clazz
     *            the class of the content
     * @param persistence
     *            the corresponding persistence service
     */
    protected <R extends Representation> void consumeMessage(Message msg,
            Class<R> clazz, GenericReceiver<? super R> persistence) {

        try {
            R content = consumeMessage(msg, clazz);
            persistence.receive(content);
        } catch (ComponentException e) {
            // TODO log error
        }
    }

    protected <R extends Representation> R consumeMessage(Message msg,
            Class<R> clazz) throws ComponentException {

        if (msg instanceof TextMessage) {

            TextMessage txtMsg = (TextMessage) msg;

            try {
                String json = txtMsg.getText();
                Gson gson = new Gson();
                R content = gson.fromJson(json, clazz);
                content.update();

                return content;
            } catch (JsonSyntaxException e) {
                throw new ValidationException(new Integer(1), "");
                // TODO add error message
            } catch (JMSException e) {
                throw new ConnectionException(new Integer(1), "");
                // TODO add error message
            }
        } else {
            throw new ValidationException(new Integer(1), "");
            // TODO add error message
        }
    }

    protected <R extends Representation> void sendAnswer(
            ConnectionFactory connectionFactory, Message message,
            R representation) {
        try {
            Connection conn = connectionFactory.createConnection();
            Session session = conn.createSession(false,
                    Session.DUPS_OK_ACKNOWLEDGE);

            Destination dest = message.getJMSReplyTo();

            MessageProducer producer = session.createProducer(dest);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);

            Gson gson = new Gson();
            String json = gson.toJson(representation);

            TextMessage answer = session.createTextMessage();
            answer.setText(json);

            producer.send(answer);
        } catch (JMSException e) {
            // TODO log error
        }
    }
}
