/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 24, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest;

import java.util.Base64;
import java.util.function.Function;

import javax.net.ssl.SSLContext;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.oscm.common.interfaces.data.Token;
import org.oscm.common.interfaces.enums.Messages;
import org.oscm.common.interfaces.exceptions.ConnectionException;
import org.oscm.common.interfaces.exceptions.ServiceException;
import org.oscm.common.util.ConfigurationManager;

/**
 * REST client for synchronous calls.
 * 
 * @author miethaner
 */
public class RestClient {

    private static final String BASIC_AUTH_PREFIX = "Basic ";

    private WebTarget target;
    private String authString = "";

    public RestClient(String base, Class<?>[] providers, SSLContext ssl,
            Token token, long expiration, String user, String password) {

        ClientBuilder builder = ClientBuilder.newBuilder();

        if (ssl != null) {
            builder = builder.sslContext(ssl);
        }

        if (providers != null) {
            for (Class<?> p : providers) {
                builder.register(p);
            }
        }

        target = builder.build().target(base);

        if (token != null) {
            authString = TokenManager.AUTHORIZATION_PREFIX + TokenManager
                    .getInstance().createAndSignToken(token, expiration);
        } else if (user != null && password != null) {
            String encoded = Base64.getEncoder()
                    .encodeToString(new String(user + ":" + password)
                            .getBytes(ConfigurationManager.CHARSET));
            authString = BASIC_AUTH_PREFIX + encoded;
        }
    }

    public <R> R get(String path, Class<R> responseType, int status)
            throws ServiceException {

        return request(path, (b) -> b.get(), responseType, status);
    }

    public <R> R post(String path, Object payload, Class<R> responseType,
            int status) throws ServiceException {

        return request(path,
                (b) -> b.post(Entity.entity(payload,
                        MediaType.APPLICATION_JSON_TYPE)),
                responseType, status);
    }

    public <R> R put(String path, Object payload, Class<R> responseType,
            int status) throws ServiceException {

        return request(path,
                (b) -> b.put(Entity.entity(payload,
                        MediaType.APPLICATION_JSON_TYPE)),
                responseType, status);
    }

    public <R> R delete(String path, Class<R> responseType, int status)
            throws ServiceException {

        return request(path, (b) -> b.delete(), responseType, status);
    }

    private <R> R request(String path, Function<Builder, Response> method,
            Class<R> responseType, int status) throws ServiceException {
        Builder builder = target.path(path) //
                .request() //
                .accept(MediaType.APPLICATION_JSON_TYPE) //
                .header(HttpHeaders.AUTHORIZATION, authString);

        Response response = null;
        try {
            response = method.apply(builder);

            if (response.getStatus() != status) {
                throw new ConnectionException(Messages.ERROR_BAD_RESPONSE,
                        response.readEntity(String.class));
            }

            return response.readEntity(responseType);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
}