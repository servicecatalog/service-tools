/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 24, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest;

import javax.ws.rs.client.AsyncInvoker;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.oscm.common.interfaces.data.Callback;
import org.oscm.common.interfaces.enums.Messages;
import org.oscm.common.interfaces.exceptions.ConnectionException;
import org.oscm.common.interfaces.exceptions.ServiceException;

/**
 * Super class for REST clients
 * 
 * @author miethaner
 */
public class RestClient {

    private static final long EXPIRATION_TIME = 300000;

    public enum Method {
        POST, PUT, DELETE;
    }

    private class RestCallback implements InvocationCallback<Response> {

        private int status;
        private Callback success;
        private Callback failure;

        public RestCallback(int status, Callback success, Callback failure) {
            this.status = status;
            this.success = success;
            this.failure = failure;
        }

        @Override
        public void completed(Response response) {
            try {
                if (response == null || response.getStatus() != status) {
                    failure.callback();
                } else {
                    success.callback();
                }
            } catch (ServiceException e) {
                // TODO Log error
            }
        }

        @Override
        public void failed(Throwable throwable) {
            try {
                failure.callback();
            } catch (ServiceException e) {
                // TODO Log error
            }
        }
    }

    protected void sendAsync(Method method, String url, Token token,
            Representation content, int status, Callback success,
            Callback failure) {

        String tokenString = TokenManager.getInstance()
                .createAndEncryptToken(token, EXPIRATION_TIME);

        AsyncInvoker invoker = getClient().target(url).request()
                .header(HttpHeaders.AUTHORIZATION,
                        RequestParameters.AUTHORIZATION_PREFIX + tokenString)
                .async();

        switch (method) {
        case POST:
            invoker.post(Entity.json(content),
                    new RestCallback(status, success, failure));
            break;
        case PUT:
            invoker.put(Entity.json(content),
                    new RestCallback(status, success, failure));
            break;
        case DELETE:
            invoker.delete(new RestCallback(status, success, failure));
            break;
        }

    }

    protected void sendSync(Method method, String url, Token token,
            Representation content, int status) throws ServiceException {

        String tokenString = TokenManager.getInstance()
                .createAndEncryptToken(token, EXPIRATION_TIME);

        Invocation.Builder builder = getClient().target(url).request().header(
                HttpHeaders.AUTHORIZATION,
                RequestParameters.AUTHORIZATION_PREFIX + tokenString);

        Response response = null;

        switch (method) {
        case POST:
            response = builder.post(Entity.json(content));
            break;
        case PUT:
            response = builder.put(Entity.json(content));
            break;
        case DELETE:
            response = builder.delete();
            break;
        }

        if (response == null || response.getStatus() != status) {
            throw new ConnectionException(Messages.ERROR);
            // TODO add error message
        }
    }

    protected Client getClient() {
        ClientConfig config = new ClientConfig();
        config.register(GsonMessageProvider.class);

        return ClientBuilder.newClient(config);
    }
}
