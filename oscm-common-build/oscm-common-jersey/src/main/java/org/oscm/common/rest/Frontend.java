/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jun 10, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

import org.oscm.common.interfaces.exceptions.ServiceException;

/**
 * Interfaces for REST endpoints
 * 
 * @author miethaner
 */
public interface Frontend {

    /**
     * Interface for HTTP GET methods.
     * 
     * @author miethaner
     *
     * @param <P>
     *            request parameters
     */
    public interface Get<P extends RequestParameters> {

        /**
         * Gets the corresponding representation of the entity with the id in
         * params and wraps it in the response.
         * 
         * @param request
         *            the request context
         * @param params
         *            the request parameters
         * @return the response with the representation
         * @throws WebApplicationException
         * @throws ServiceException
         */
        public Response getItem(Request request, P params)
                throws WebApplicationException, ServiceException;

        /**
         * Gets all valid entity representations and wraps them in the response.
         * 
         * @param request
         *            the request context
         * @param params
         *            the request parameters
         * @return the response with the representations
         * @throws WebApplicationException
         * @throws ServiceException
         */
        public Response getCollection(Request request, P params)
                throws WebApplicationException, ServiceException;
    }

    /**
     * Interface for HTTP POST methods.
     * 
     * @author miethaner
     *
     * @param <R>
     *            a representation
     * @param <P>
     *            request parameters
     */
    public interface Post<R extends Representation, P extends RequestParameters> {

        /**
         * Creates a new entity from the given representation and returns its id
         * within the location header of the response.
         * 
         * @param request
         *            the request context
         * @param content
         *            the representation to create
         * @param params
         *            the request parameters
         * @return the response with the location
         * @throws WebApplicationException
         * @throws ServiceException
         */
        public Response postCollection(Request request, R content, P params)
                throws WebApplicationException, ServiceException;
    }

    /**
     * Interface for HTTP PUT methods.
     * 
     * @author miethaner
     *
     * @param <R>
     *            a representation
     * @param <P>
     *            request parameters
     */
    public interface Put<R extends Representation, P extends RequestParameters> {

        /**
         * Updates the entity with the id in params with the given
         * representation.
         * 
         * @param request
         *            the request context
         * @param content
         *            the representation to update
         * @param params
         *            the request parameters
         * @return the response without content
         * @throws WebApplicationException
         * @throws ServiceException
         */
        public Response putItem(Request request, R content, P params)
                throws WebApplicationException, ServiceException;
    }

    /**
     * Interface for HTTP DELETE methods.
     * 
     * @author miethaner
     *
     * @param <P>
     *            request parameters
     */
    public interface Delete<P extends RequestParameters> {

        /**
         * Deletes the entity with the id in params.
         * 
         * @param request
         *            the request context
         * @param params
         *            the request parameters
         * @return the response without content
         * @throws WebApplicationException
         * @throws ServiceException
         */
        public Response deleteItem(Request request, P params)
                throws WebApplicationException, ServiceException;
    }

    /**
     * Interface for all standard methods.
     * 
     * @author miethaner
     *
     * @param <R>
     *            a representation
     * @param <P>
     *            request parameters
     */
    public interface Crud<R extends Representation, P extends RequestParameters>
            extends Get<P>, Post<R, P>, Put<R, P>, Delete<P> {
    }

}
