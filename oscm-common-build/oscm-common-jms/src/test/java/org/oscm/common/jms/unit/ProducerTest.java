/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 23, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.jms.unit;

import static org.junit.Assert.fail;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.junit.Test;
import org.mockito.Mockito;
import org.oscm.common.interfaces.data.DataType;
import org.oscm.common.interfaces.enums.Operation;
import org.oscm.common.interfaces.exceptions.ConnectionException;
import org.oscm.common.jms.Producer;
import org.oscm.common.jms.Representation;

/**
 * Unit test for Producer
 * 
 * @author miethaner
 */
public class ProducerTest extends Producer {

    private interface TestType extends DataType {
    }

    private class TestRep extends Representation implements TestType {

        @Override
        public void update() {
        }

    }

    @Test
    public void testInitPositive() throws Exception {

        ConnectionFactory factory = Mockito.mock(ConnectionFactory.class);
        Destination dest = Mockito.mock(Destination.class);
        Connection conn = Mockito.mock(Connection.class);
        Session session = Mockito.mock(Session.class);
        MessageProducer producer = Mockito.mock(MessageProducer.class);

        Mockito.when(factory.createConnection()).thenReturn(conn);
        Mockito.when(conn.createSession(false, Session.DUPS_OK_ACKNOWLEDGE))
                .thenReturn(session);
        Mockito.when(session.createProducer(dest)).thenReturn(producer);

        init(factory, dest);
    }

    @Test
    public void testInitNegative() throws Exception {

        ConnectionFactory factory = Mockito.mock(ConnectionFactory.class);
        Destination dest = Mockito.mock(Destination.class);

        Mockito.when(factory.createConnection()).thenThrow(
                new JMSException("because REASONS!!!"));

        init(factory, dest);
    }

    @Test
    public void testClosePositive() throws Exception {

        ConnectionFactory factory = Mockito.mock(ConnectionFactory.class);
        Destination dest = Mockito.mock(Destination.class);
        Connection conn = Mockito.mock(Connection.class);
        Session session = Mockito.mock(Session.class);
        MessageProducer producer = Mockito.mock(MessageProducer.class);

        Mockito.when(factory.createConnection()).thenReturn(conn);
        Mockito.when(conn.createSession(false, Session.DUPS_OK_ACKNOWLEDGE))
                .thenReturn(session);
        Mockito.when(session.createProducer(dest)).thenReturn(producer);

        init(factory, dest);

        close();
    }

    @Test
    public void testCloseNegativeConnection() throws Exception {
        close();
    }

    @Test
    public void testCloseNegativeClose() throws Exception {

        ConnectionFactory factory = Mockito.mock(ConnectionFactory.class);
        Destination dest = Mockito.mock(Destination.class);
        Connection conn = Mockito.mock(Connection.class);
        Session session = Mockito.mock(Session.class);
        MessageProducer producer = Mockito.mock(MessageProducer.class);

        Mockito.when(factory.createConnection()).thenReturn(conn);
        Mockito.when(conn.createSession(false, Session.DUPS_OK_ACKNOWLEDGE))
                .thenReturn(session);
        Mockito.when(session.createProducer(dest)).thenReturn(producer);

        Mockito.doThrow(new JMSException("because REASONS!!!")).when(conn)
                .close();

        init(factory, dest);

        close();
    }

    @Test
    public void testProducePositive() throws Exception {

        ConnectionFactory factory = Mockito.mock(ConnectionFactory.class);
        Destination dest = Mockito.mock(Destination.class);
        Connection conn = Mockito.mock(Connection.class);
        Session session = Mockito.mock(Session.class);
        MessageProducer producer = Mockito.mock(MessageProducer.class);
        TextMessage msg = Mockito.mock(TextMessage.class);

        Mockito.when(factory.createConnection()).thenReturn(conn);
        Mockito.when(conn.createSession(false, Session.DUPS_OK_ACKNOWLEDGE))
                .thenReturn(session);
        Mockito.when(session.createProducer(dest)).thenReturn(producer);
        Mockito.when(session.createTextMessage()).thenReturn(msg);

        init(factory, dest);

        TestRep rep = new TestRep();
        rep.setId(new Long(1));
        rep.setETag(new Long(1));
        rep.setLastOperation(Operation.CREAT);
        rep.setVersion(new Integer(1));

        produceMessage(rep);
    }

    @Test
    public void testProduceNegativeConnection() throws Exception {

        TestRep rep = new TestRep();
        rep.setId(new Long(1));
        rep.setETag(new Long(1));
        rep.setLastOperation(Operation.CREAT);
        rep.setVersion(new Integer(1));

        try {
            produceMessage(rep);
            fail();
        } catch (ConnectionException e) {
        }
    }

    @Test
    public void testProduceNegativeSend() throws Exception {

        ConnectionFactory factory = Mockito.mock(ConnectionFactory.class);
        Destination dest = Mockito.mock(Destination.class);
        Connection conn = Mockito.mock(Connection.class);
        Session session = Mockito.mock(Session.class);
        MessageProducer producer = Mockito.mock(MessageProducer.class);

        Mockito.when(factory.createConnection()).thenReturn(conn);
        Mockito.when(conn.createSession(false, Session.DUPS_OK_ACKNOWLEDGE))
                .thenReturn(session);
        Mockito.when(session.createProducer(dest)).thenReturn(producer);
        Mockito.when(session.createTextMessage()).thenThrow(
                new JMSException("because REASONS!!!"));

        init(factory, dest);

        TestRep rep = new TestRep();
        rep.setId(new Long(1));
        rep.setETag(new Long(1));
        rep.setLastOperation(Operation.CREAT);
        rep.setVersion(new Integer(1));

        try {
            produceMessage(rep);
            fail();
        } catch (ConnectionException e) {
        }
    }

}
