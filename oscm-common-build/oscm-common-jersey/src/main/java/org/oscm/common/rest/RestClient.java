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
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.oscm.common.interfaces.data.Token;
import org.oscm.common.interfaces.enums.Messages;
import org.oscm.common.interfaces.exceptions.ConnectionException;
import org.oscm.common.interfaces.exceptions.ServiceException;
import org.oscm.common.rest.filters.AuthenticationFilter;

/**
 * Super class for REST clients
 * 
 * @author miethaner
 */
public class RestClient {

    public enum Method {
        POST, PUT, DELETE;
    }

    private Class<?>[] providers;

    public RestClient(Class<?>... providers) {
        this.providers = providers;
    }

    protected void send(Method method, String url, Token token, long expiration,
            Object content, int status) throws ServiceException {

        String tokenString = TokenManager.getInstance()
                .createAndEncryptToken(token, expiration);

        Invocation.Builder builder = getClient().target(url).request().header(
                HttpHeaders.AUTHORIZATION,
                AuthenticationFilter.AUTHORIZATION_PREFIX + tokenString);

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
            throw new ConnectionException(Messages.BAD_RESPONSE);
        }
    }

    protected Client getClient() {
        ClientConfig config = new ClientConfig();

        for (Class<?> p : providers) {
            config.register(p);
        }

        return ClientBuilder.newClient(config);
    }
}
