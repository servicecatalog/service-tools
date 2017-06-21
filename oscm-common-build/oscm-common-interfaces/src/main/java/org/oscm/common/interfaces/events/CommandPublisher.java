/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jun 15, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.events;

import org.oscm.common.interfaces.data.Command;
import org.oscm.common.interfaces.exceptions.ServiceException;

/**
 * Interface for command publishers.
 * 
 * @author miethaner
 */
public interface CommandPublisher {

    /**
     * Publishes the given command to the backend. The backend will use the
     * given handler to return responses.
     * 
     * @param command
     *            the command entity
     * @param handler
     *            the result handler
     * @throws ServiceException
     */
    public void publish(Command command, ResultHandler handler)
            throws ServiceException;
}
