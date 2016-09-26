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
import org.oscm.common.interfaces.exceptions.CacheException;
import org.oscm.common.interfaces.exceptions.ComponentException;
import org.oscm.common.interfaces.exceptions.ConcurrencyException;
import org.oscm.common.interfaces.exceptions.InternalException;
import org.oscm.common.interfaces.exceptions.NotFoundException;
import org.oscm.common.interfaces.exceptions.ValidationException;

/**
 * Generic interfaces for operations on data entities.
 * 
 * @author miethaner
 */
public interface GenericDataPersistence<D extends DataType> {

    /**
     * Creates an entity with the given content and parameters.
     * 
     * @param content
     *            the entity content
     * @return the ID of the created entity
     * @throws ValidationException
     *             if parameters are not valid
     * @throws InternalException
     *             if an unexpected error occurs
     */
    public D create(D content) throws ComponentException;

    /**
     * Reads the entity specified by the given parameters.
     * 
     * @param params
     *            the read parameters
     * @return the specified entity
     * @throws ValidationException
     *             if parameters are not valid
     * @throws NotFoundException
     *             if entity does not exists
     * @throws InternalException
     *             if an unexpected error occurs
     */
    public D read(Long id) throws ComponentException;

    /**
     * Reads the entity specified by the given parameters. Checks for
     * modification and throws exception if not modified.
     * 
     * @param params
     *            the read parameters
     * @param etag
     *            the etag to compare
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
    public D read(Long id, Long etag) throws ComponentException;

    /**
     * Reads all entities that have not been confirmed to be published.
     * 
     * @return the list of unpublished entities
     * @throws InternalException
     *             if an unexpected error occurs
     */
    public List<D> readAllUnpublished() throws ComponentException;

    /**
     * Confirms that an entity was published.
     * 
     * @param id
     *            the id of the published entity
     * @throws ValidationException
     *             if parameters are not valid
     * @throws NotFoundException
     *             if entity does not exists
     * @throws InternalException
     *             if an unexpected error occurs
     */
    public void confirmPublish(Long id) throws ComponentException;

    /**
     * Updates the entity specified by the given parameters with the given
     * content and parameters.
     * 
     * @param content
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
    public D update(D content) throws ComponentException;

    /**
     * Deletes the entity specified in the given parameters.
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
