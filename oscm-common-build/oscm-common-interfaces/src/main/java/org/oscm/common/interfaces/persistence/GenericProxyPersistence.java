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

/**
 * Interface for operations on proxy entities
 * 
 * @author miethaner
 */
public interface GenericProxyPersistence<D extends DataType> {

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
     * Reads all valid entities.
     * 
     * @return list of entities
     */
    public List<D> readAll();

    /**
     * Merges the proxy entity into the persistence
     * 
     * @param content
     *            the entity content
     * @throws ComponentException
     */
    public void merge(D content) throws ComponentException;
}
