/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jun 15, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.services;

import java.util.List;

import org.oscm.common.interfaces.data.DataType;
import org.oscm.common.interfaces.data.ParameterType;
import org.oscm.common.interfaces.exceptions.CacheException;
import org.oscm.common.interfaces.exceptions.ServiceException;
import org.oscm.common.interfaces.exceptions.ConcurrencyException;
import org.oscm.common.interfaces.exceptions.InternalException;
import org.oscm.common.interfaces.exceptions.NotFoundException;
import org.oscm.common.interfaces.exceptions.ValidationException;

/**
 * Generic interfaces for CRUD services
 * 
 * @author miethaner
 */
public interface GenericService {

    public interface Create<D extends DataType, P extends ParameterType> {

        /**
         * Creates the given entity with the given parameters
         * 
         * @param content
         *            the entity content
         * @param params
         *            the creation parameters
         * @return the ID of the created entity
         * @throws ValidationException
         *             if parameters are not valid
         * @throws InternalException
         *             if an unexpected error occurs
         */
        public Long create(D content, P params) throws ServiceException;
    }

    public interface Read<D extends DataType, P extends ParameterType> {

        /**
         * Reads the entity specified by the given parameters
         * 
         * @param params
         *            the read parameters
         * @return the specified entity
         * @throws ValidationException
         *             if parameters are not valid
         * @throws NotFoundException
         *             if entity does not exists
         * @throws CacheException
         *             if the etag is equals the current one (not modified)
         * @throws InternalException
         *             if an unexpected error occurs
         */
        public D read(P params) throws ServiceException;

        /**
         * Reads all entities specified by the given parameters
         * 
         * @param params
         *            the read parameters
         * @return the list of entities
         * @throws ValidationException
         *             if parameters are not valid
         * @throws InternalException
         *             if an unexpected error occurs
         */
        public List<D> readAll(P params) throws ServiceException;
    }

    public interface Update<D extends DataType, P extends ParameterType> {

        /**
         * Updates the given entity with the given parameters.
         * 
         * @param content
         *            the entity content
         * @param params
         *            the update parameters
         * @return the ID of the created entity
         * @throws ValidationException
         *             if parameters are not valid
         * @throws NotFoundException
         *             if entity does not exists
         * @throws ConcurrencyException
         *             if the etag (if present) is not equals the current one
         * @throws InternalException
         *             if an unexpected error occurs
         */
        public void update(D content, P params) throws ServiceException;
    }

    public interface Delete<P extends ParameterType> {

        /**
         * Deletes the entity specified in the given parameters
         * 
         * @param params
         *            the deletion parameters
         * @throws ValidationException
         *             if parameters are not valid
         * @throws NotFoundException
         *             if entity does not exists
         * @throws InternalException
         *             if an unexpected error occurs
         */
        public void delete(P params) throws ServiceException;
    }

    public interface Crud<D extends DataType, P extends ParameterType>
            extends Create<D, P>, Read<D, P>, Update<D, P>, Delete<P> {
    }
}
