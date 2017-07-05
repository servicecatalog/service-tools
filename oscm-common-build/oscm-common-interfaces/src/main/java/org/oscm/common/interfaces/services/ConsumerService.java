/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jul 4, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.services;

import org.oscm.common.interfaces.data.Event;
import org.oscm.common.interfaces.exceptions.ServiceException;

/**
 * Functional interface for consumer methods.
 * 
 * @author miethaner
 */
public interface ConsumerService {

    /**
     * Processes a given event.
     * 
     * @param event
     *            the event to process
     * @throws ServiceException
     */
    public void process(Event event) throws ServiceException;
}
