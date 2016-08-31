/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jun 9, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest;

import java.net.URI;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.oscm.common.interfaces.enums.Messages;
import org.oscm.common.interfaces.exceptions.ComponentException;
import org.oscm.common.interfaces.exceptions.NotFoundException;
import org.oscm.common.interfaces.exceptions.SecurityException;
import org.oscm.common.interfaces.exceptions.ValidationException;
import org.oscm.common.interfaces.security.SecurityToken;

/**
 * Super class for REST resources and their endpoints.
 * 
 * @author miethaner
 */
public abstract class Resource {

    public static final String PATH_VERSION = "/{"
            + RequestParameters.PARAM_VERSION + "}";
    public static final String PATH_ID = "/{" + RequestParameters.PARAM_ID
            + "}";

    /**
     * Wrapper for backend GET commands. Prepares, validates and revises data
     * for commands and assembles responses.
     * 
     * @param request
     *            the request context
     * @param backend
     *            the backend command
     * @param params
     *            the request parameters
     * @param id
     *            true if id needs to be validated
     * @return the response with representation or -collection
     */
    protected <R extends Representation, P extends RequestParameters> Response get(
            Request request, Backend.Get<R, P> backend, P params, boolean id)
            throws WebApplicationException, ComponentException {

        extractProperties(request, params);

        prepareData(params, id, null, false);

        Representation item = backend.get(params);

        reviseData(params, item);

        String etag = null;
        if (item.getETag() != null) {
            etag = item.getETag().toString();
        }

        return Response.ok(item).tag(etag).build();
    }

    /**
     * Wrapper for backend POST commands. Prepares, validates and revises data
     * for commands and assembles responses.
     * 
     * @param request
     *            the request context
     * @param backend
     *            the backend command
     * @param content
     *            the representation to create
     * @param params
     *            the request parameters
     * @return the response with the new location
     */
    protected <R extends Representation, P extends RequestParameters> Response post(
            Request request, Backend.Post<R, P> backend, R content, P params)
            throws WebApplicationException, ComponentException {

        extractProperties(request, params);

        prepareData(params, false, content, true);

        Object newId = backend.post(content, params);

        ContainerRequestContext cr = (ContainerRequestContext) request;
        UriBuilder builder = cr.getUriInfo().getAbsolutePathBuilder();
        URI uri = builder.path(newId.toString()).build();

        return Response.created(uri).build();
    }

    /**
     * Wrapper for backend PUT commands. Prepares, validates and revises data
     * for commands and assembles responses.
     * 
     * @param request
     *            the request context
     * @param backend
     *            the backend command
     * @param content
     *            the representation to update
     * @param params
     *            the request parameters
     * @return the response without content
     */
    protected <R extends Representation, P extends RequestParameters> Response put(
            Request request, Backend.Put<R, P> backend, R content, P params)
            throws WebApplicationException, ComponentException {

        extractProperties(request, params);

        prepareData(params, true, content, true);

        content.setId(params.getId());
        content.setETag(params.getETag());

        backend.put(content, params);

        return Response.noContent().build();
    }

    /**
     * Wrapper for backend DELETE commands. Prepares, validates and revises data
     * for commands and assembles responses.
     * 
     * @param request
     *            the request context
     * @param backend
     *            the backend command
     * @param params
     *            the request parameters
     * @return the response without content
     */
    protected <P extends RequestParameters> Response delete(Request request,
            Backend.Delete<P> backend, P params)
            throws WebApplicationException, ComponentException {

        extractProperties(request, params);

        prepareData(params, true, null, false);

        backend.delete(params);

        return Response.noContent().build();
    }

    /**
     * Extracts the version number from the container request properties. Throws
     * Exception if property is null.
     * 
     * @param request
     *            the container request
     * @param params
     *            the parameters to inject to
     * @throws WebApplicationException
     */
    protected void extractProperties(Request request, RequestParameters params)
            throws ComponentException {

        ContainerRequestContext cr = (ContainerRequestContext) request;
        Object version = cr.getProperty(RequestParameters.PARAM_VERSION);

        if (version == null) {
            throw new NotFoundException(Messages.INVALID_VERSION.error(),
                    Messages.INVALID_VERSION.message());
        }
        params.setVersion((Integer) version);

        Object token = cr.getProperty(RequestParameters.PARAM_TOKEN);

        if (token == null) {
            throw new SecurityException(Messages.NOT_AUTHENTICATED.error(),
                    Messages.NOT_AUTHENTICATED.message());
        }
        params.setSecurityToken((SecurityToken) token);
    }

    /**
     * Prepares the data for the backend call
     * 
     * @param params
     *            the injected parameters with the target version
     * @param withId
     *            if true validate resource id
     * @param rep
     *            the representation (can be null)
     * @throws WebApplicationException
     */
    protected void prepareData(RequestParameters params, boolean withId,
            Representation rep, boolean withRep)
            throws WebApplicationException, ComponentException {

        if (withId) {
            if (params.getId() == null) {
                throw new NotFoundException(Messages.INVALID_ID.error(),
                        Messages.INVALID_ID.message());
            }
        }

        params.validateETag();
        params.validateParameters();
        params.update();

        if (withRep) {

            if (rep == null) {
                throw new ValidationException(Messages.NO_CONTENT.error(),
                        Messages.NO_CONTENT.message());
            }

            rep.validateContent();

            rep.setVersion(params.getVersion());
            rep.update();
        }
    }

    /**
     * Revises the data after the backend call
     * 
     * @param params
     *            the params with the version to convert to
     * @param rep
     *            the representation
     */
    protected void reviseData(RequestParameters params, Representation rep) {

        if (rep != null) {
            rep.setVersion(params.getVersion());
            rep.convert();
        }
    }
}
