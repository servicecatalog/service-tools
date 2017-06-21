/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 1, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.services;

import java.util.List;

import org.oscm.common.interfaces.data.Command;
import org.oscm.common.interfaces.data.Event;
import org.oscm.common.interfaces.exceptions.ServiceException;

/**
 * Functional interface for command methods.
 * 
 * @author miethaner
 */
@FunctionalInterface
public interface CommandService {

    /**
     * Executes the given command and returns the resulting list of events.
     * 
     * @param command
     *            the command to execute
     * @return the list of events
     * @throws ServiceException
     */
    public List<Event> execute(Command command) throws ServiceException;
}
