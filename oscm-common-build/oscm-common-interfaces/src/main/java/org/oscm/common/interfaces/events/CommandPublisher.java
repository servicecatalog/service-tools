/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jun 15, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.events;

import java.util.List;
import java.util.UUID;

import org.oscm.common.interfaces.data.Command;
import org.oscm.common.interfaces.data.Result;
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

    /**
     * Gets the result for the given command id. If no result can be found, null
     * is returned instead.
     * 
     * @param id
     *            the command id
     * @return the result or null
     */
    public Result getResult(UUID id);

    /**
     * Gets all result for this publisher.
     * 
     * @return the list of results
     */
    public List<Result> getAllResults();
}
