/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 6, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.services;

import java.util.List;

import org.oscm.common.interfaces.data.Event;
import org.oscm.common.interfaces.data.Token;
import org.oscm.common.interfaces.exceptions.CacheException;
import org.oscm.common.interfaces.exceptions.NotFoundException;
import org.oscm.common.interfaces.exceptions.SecurityException;
import org.oscm.common.interfaces.exceptions.ServiceException;
import org.oscm.common.interfaces.exceptions.ValidationException;

/**
 * Functional Interface for query methods.
 * 
 * @author miethaner
 */
@FunctionalInterface
public interface QueryService {

    /**
     * Queries a event source with the given event and token and returns the
     * resulting list of events.
     * 
     * @param event
     *            the query parameters as event
     * @param token
     *            the security token
     * @return the list of events
     * @throws CacheException
     *             if the etag matches
     * @throws NotFoundException
     *             if a specified event could not be found
     * @throws SecurityException
     *             if the token owner is not allowed to access a specified event
     * @throws ValidationException
     *             if there are inconsistencies within the given data or related
     *             events
     */
    public List<Event> query(Event event, Token token) throws ServiceException;
}
