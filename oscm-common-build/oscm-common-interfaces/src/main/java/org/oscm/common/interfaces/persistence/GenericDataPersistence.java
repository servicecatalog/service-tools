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
import org.oscm.common.interfaces.exceptions.ComponentException;

/**
 * 
 * Generic interfaces for CRUD persistence
 * 
 * @author miethaner
 */
public interface GenericDataPersistence<D extends DataType> {

    /**
     * Creates an entity with the given content and parameters
     * 
     * @param content
     *            the entity content
     * @return the ID of the created entity
     * @throws ComponentException
     */
    public D create(D content) throws ComponentException;

    /**
     * Reads the entity specified by the given parameters
     * 
     * @param params
     *            the read parameters
     * @return the specified entity
     * @throws ComponentException
     */
    public D read(Long id) throws ComponentException;

    /**
     * Reads all entities that have not been confirmed to be published.
     * 
     * @return the list of unpublished entities
     * @throws ComponentException
     */
    public List<D> readAllUnpublished() throws ComponentException;

    /**
     * Confirms that an entity was published.
     * 
     * @param id
     *            the id of the published entity
     * @throws ComponentException
     */
    public void confirmPublish(Long id) throws ComponentException;

    /**
     * Updates the entity specified by the given parameters with the given
     * content and parameters
     * 
     * @param content
     *            the entity content
     * @return the ID of the created entity
     * @throws ComponentException
     */
    public D update(D content) throws ComponentException;

    /**
     * Deletes the entity specified in the given parameters
     * 
     * @throws ComponentException
     */
    public D delete(Long id) throws ComponentException;

}
