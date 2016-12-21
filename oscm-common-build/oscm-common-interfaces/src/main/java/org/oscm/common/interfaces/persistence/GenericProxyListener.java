/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Dec 6, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.persistence;

import org.oscm.common.interfaces.data.DataType;
import org.oscm.common.interfaces.exceptions.ServiceException;
import org.oscm.common.interfaces.exceptions.InternalException;
import org.oscm.common.interfaces.exceptions.NotFoundException;

/**
 * Generic interface for merging incoming events into persistence
 * 
 * @author miethaner
 */
public interface GenericProxyListener<D extends DataType> {

    /**
     * Merges the proxy entity into the persistence according the last
     * operation.
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
    public void handleEvent(D content) throws ServiceException;
}
