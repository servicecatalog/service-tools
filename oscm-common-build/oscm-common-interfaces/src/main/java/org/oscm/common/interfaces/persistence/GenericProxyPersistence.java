/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jun 27, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.persistence;

import java.util.List;

import org.oscm.common.interfaces.data.DataType;
import org.oscm.common.interfaces.exceptions.ComponentException;
import org.oscm.common.interfaces.exceptions.InternalException;
import org.oscm.common.interfaces.exceptions.NotFoundException;
import org.oscm.common.interfaces.exceptions.ValidationException;

/**
 * Generic interface for operations on proxy entities.
 * 
 * @author miethaner
 */
public interface GenericProxyPersistence<D extends DataType> {

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
     * Reads all valid entities.
     * 
     * @return list of entities
     * @throws InternalException
     *             if an unexpected error occurs
     */
    public List<D> readAll() throws ComponentException;

    /**
     * Merges the proxy entity into the persistence.
     * 
     * @param content
     *            the entity content
     * @throws ValidationException
     *             if parameters are not valid
     * @throws NotFoundException
     *             if entity does not exists
     * @throws InternalException
     *             if an unexpected error occurs
     */
    public void merge(D content) throws ComponentException;
}
