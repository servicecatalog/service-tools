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
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.oscm.common.interfaces.data.Callback;
import org.oscm.common.interfaces.exceptions.ComponentException;

/**
 * Super class for REST clients
 * 
 * @author miethaner
 */
public class RestClient {

    protected void sendPost(String url, Representation content, int status,
            Callback success, Callback failure) {

        getClient().target(url).request().async().post(Entity.json(content),
                new InvocationCallback<Response>() {

                    @Override
                    public void completed(Response response) {
                        try {
                            if (response == null
                                    || response.getStatus() != status) {
                                failure.callback();
                            } else {
                                success.callback();
                            }
                        } catch (ComponentException e) {
                            // TODO Log error
                        }

                    }

                    @Override
                    public void failed(Throwable throwable) {
                        try {
                            failure.callback();
                        } catch (ComponentException e) {
                            // TODO Log error
                        }
                    }
                });
    }

    protected Client getClient() {
        ClientConfig config = new ClientConfig();
        config.register(GsonMessageProvider.class);

        return ClientBuilder.newClient(config);
    }
}
