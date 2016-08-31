/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 25, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest.unit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;
import org.mockito.Mockito;
import org.oscm.common.interfaces.exceptions.ConnectionException;
import org.oscm.common.rest.Representation;
import org.oscm.common.rest.RestClient;

/**
 * Unit test for RestClient
 * 
 * @author miethaner
 */
public class RestClientTest extends RestClient {

    private class MockRepresentation extends Representation {
        @Override
        public void validateContent() throws WebApplicationException {
        }

        @Override
        public void update() {
        }

        @Override
        public void convert() {
        }
    }

    private Client client;

    @Override
    protected Client getClient() {
        return client;
    }

    @SuppressWarnings("boxing")
    @Test
    public void testPostPositive() throws Exception {
        client = Mockito.mock(Client.class);
        WebTarget target = Mockito.mock(WebTarget.class,
                Mockito.RETURNS_DEEP_STUBS);
        Response response = Mockito.mock(Response.class);

        MockRepresentation rep = new MockRepresentation();

        Mockito.when(client.target(Mockito.anyString())).thenReturn(target);
        Mockito.when(target.request().post(Mockito.any(Entity.class)))
                .thenReturn(response);
        Mockito.when(response.getStatus()).thenReturn(
                new Integer(Status.OK.getStatusCode()));

        sendPost("", rep, Status.OK.getStatusCode());
    }

    @SuppressWarnings("boxing")
    @Test
    public void testPostNegative() throws Exception {
        client = Mockito.mock(Client.class);
        WebTarget target = Mockito.mock(WebTarget.class,
                Mockito.RETURNS_DEEP_STUBS);
        Response response = Mockito.mock(Response.class);

        MockRepresentation rep = new MockRepresentation();

        Mockito.when(client.target(Mockito.anyString())).thenReturn(target);
        Mockito.when(target.request().post(Mockito.any(Entity.class)))
                .thenReturn(response);
        Mockito.when(response.getStatus()).thenReturn(
                new Integer(Status.BAD_REQUEST.getStatusCode()));

        try {
            sendPost("", rep, Status.OK.getStatusCode());
            fail();
        } catch (ConnectionException e) {
        }
    }

    @Test
    public void testGetClientPositive() {
        Client client = super.getClient();

        assertNotNull(client);
    }
}
