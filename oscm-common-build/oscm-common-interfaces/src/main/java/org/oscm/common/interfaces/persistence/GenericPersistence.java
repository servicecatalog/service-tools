/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jun 23, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.persistence;

import java.util.List;

import org.oscm.common.interfaces.data.DataType;
import org.oscm.common.interfaces.data.ParameterType;
import org.oscm.common.interfaces.exceptions.ComponentException;
import org.oscm.common.interfaces.exceptions.ConcurrencyException;
import org.oscm.common.interfaces.exceptions.InternalException;
import org.oscm.common.interfaces.exceptions.NotFoundException;
import org.oscm.common.interfaces.exceptions.ValidationException;

/**
 * Generic interfaces for operations on entities.
 * 
 * @author miethaner
 */
public interface GenericPersistence {

    public interface Create<D extends DataType> {

        /**
         * Creates an entity with the given content and parameters.
         * 
         * @param entity
         *            the entity content
         * @return the ID of the created entity
         * @throws ValidationException
         *             if parameters are not valid
         * @throws InternalException
         *             if an unexpected error occurs
         */
        public D create(D entity) throws ComponentException;
    }

    public interface Read<D extends DataType, P extends ParameterType> {

        /**
         * Reads the entity specified by the given parameters.
         * 
         * @param params
         *            the parameters
         * @return the specified entity
         * @throws ValidationException
         *             if parameters are not valid
         * @throws NotFoundException
         *             if entity does not exists
         * @throws InternalException
         *             if an unexpected error occurs
         */
        public D read(P params) throws ComponentException;

        /**
         * Reads all entities specified by the given parameters.
         * 
         * @param params
         *            the parameters
         * @return the list of entities
         */
        public List<D> readAll(P params);
    }

    public interface Update<D extends DataType> {
        /**
         * Updates the entity specified by the given parameters with the given
         * content and parameters.
         * 
         * @param entity
         *            the entity content
         * @return the ID of the created entity
         * @throws ValidationException
         *             if parameters are not valid
         * @throws NotFoundException
         *             if entity does not exists
         * @throws ConcurrencyException
         *             if the etag is not equals the current one
         * @throws InternalException
         *             if an unexpected error occurs
         */
        public D update(D entity) throws ComponentException;
    }

    public interface Delete<D extends DataType> {
        /**
         * Deletes the entity specified in the given parameters.
         * 
         * @param id
         *            the id of the entity
         * @return the deleted entity
         * 
         * @throws ValidationException
         *             if parameters are not valid
         * @throws NotFoundException
         *             if entity does not exists
         * @throws InternalException
         *             if an unexpected error occurs
         */
        public D delete(Long id) throws ComponentException;
    }

    public interface Crud<D extends DataType, P extends ParameterType>
            extends Create<D>, Read<D, P>, Update<D>, Delete<D> {
    }
}
