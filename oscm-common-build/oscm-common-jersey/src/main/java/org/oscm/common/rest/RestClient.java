/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 24, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.oscm.common.interfaces.exceptions.ComponentException;
import org.oscm.common.interfaces.exceptions.ConnectionException;

/**
 * Super class for REST clients
 * 
 * @author miethaner
 */
public class RestClient {

    protected void sendPost(String url, Representation content, int status)
            throws ComponentException {

        WebTarget target = getClient().target(url);

        Response response = target.request().post(Entity.json(content));

        if (response == null || response.getStatus() != status) {
            throw new ConnectionException(new Integer(1), "");
            // TODO add error message
        }
    }

    protected Client getClient() {
        ClientConfig config = new ClientConfig();
        config.register(GsonMessageProvider.class);

        return ClientBuilder.newClient(config);
    }
}
