/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 24, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest;

import java.util.function.Function;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.oscm.common.interfaces.enums.Messages;
import org.oscm.common.interfaces.exceptions.ConnectionException;
import org.oscm.common.interfaces.exceptions.ServiceException;
import org.oscm.common.interfaces.keys.ApplicationKey;

/**
 * REST target client for synchronous calls.
 * 
 * @author miethaner
 */
public class RestClient {

    private WebTarget target;
    private MediaType mediaType;

    /**
     * Creates a new client for the given application with the given target URL.
     * The client uses the configured features and providers from the
     * {@link ClientManager}. Request and responses use the given media type.
     *
     * @param application
     *            the application key
     * @param target
     *            the target URL
     */
    public RestClient(ApplicationKey application, String target,
            MediaType mediaType) {

        this.target = ClientManager.getInstance().getTarget(application,
                target);
        this.mediaType = mediaType;
    }

    /**
     * Gets the resource at the given path relative to the target. The response
     * will be checked for the given status and parsed to the given type.
     * 
     * @param path
     *            the resource path
     * @param responseType
     *            the response type
     * @param status
     *            the expected response status
     * @return the response payload
     * @throws ServiceException
     */
    public <R> R get(String path, Class<R> responseType, int status)
            throws ServiceException {

        return request(path, (b) -> b.get(), responseType, status);
    }

    /**
     * Posts the given payload to the resource at the given path relative to the
     * target. The response will be checked for the given status and parsed to
     * the given type.
     * 
     * @param path
     *            the resource path
     * @param payload
     *            the request payload
     * @param responseType
     *            the response type
     * @param status
     *            the expected response status
     * @return the response payload
     * @throws ServiceException
     */
    public <R> R post(String path, Object payload, Class<R> responseType,
            int status) throws ServiceException {

        return request(path, (b) -> b.post(Entity.entity(payload, mediaType)),
                responseType, status);
    }

    /**
     * Puts the given payload to the resource at the given path relative to the
     * target. The response will be checked for the given status and parsed to
     * the given type.
     * 
     * @param path
     *            the resource path
     * @param payload
     *            the request payload
     * @param responseType
     *            the response type
     * @param status
     *            the expected response status
     * @return the response payload
     * @throws ServiceException
     */
    public <R> R put(String path, Object payload, Class<R> responseType,
            int status) throws ServiceException {

        return request(path, (b) -> b.put(Entity.entity(payload, mediaType)),
                responseType, status);
    }

    /**
     * Deletes the resource at the given path relative to the target. The
     * response will be checked for the given status and parsed to the given
     * type.
     * 
     * @param path
     *            the resource path
     * @param responseType
     *            the response type
     * @param status
     *            the expected response status
     * @return the response payload
     * @throws ServiceException
     */
    public <R> R delete(String path, Class<R> responseType, int status)
            throws ServiceException {

        return request(path, (b) -> b.delete(), responseType, status);
    }

    private <R> R request(String path, Function<Builder, Response> method,
            Class<R> responseType, int status) throws ServiceException {
        Builder builder = target.path(path) //
                .request() //
                .accept(mediaType);

        Response response = null;
        try {
            response = method.apply(builder);

            if (response.getStatus() != status) {
                throw new ConnectionException(Messages.ERROR_BAD_RESPONSE,
                        response.readEntity(String.class));
            }

            return response.readEntity(responseType);
        } catch (ProcessingException e) {
            throw new ConnectionException(Messages.ERROR_CONNECTION_FAILURE, e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
}