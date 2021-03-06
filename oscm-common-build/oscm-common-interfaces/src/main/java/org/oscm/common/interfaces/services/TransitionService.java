/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 12, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.services;

import java.util.List;

import org.oscm.common.interfaces.data.Event;
import org.oscm.common.interfaces.exceptions.ServiceException;

/**
 * Functional interface for transition methods.
 * 
 * @author miethaner
 */
@FunctionalInterface
public interface TransitionService {

    /**
     * Processes a given event and maps it to a list of different events. If
     * this method is used in a scheduled task, returning null will stop further
     * execution.
     * 
     * @param event
     *            the event to process
     * @return the list of events or null
     * @throws ServiceException
     */
    public List<Event> process(Event event) throws ServiceException;
}
