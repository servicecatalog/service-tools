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
 * @author miethaner
 *
 */
@FunctionalInterface
public interface CommandService {

    public List<Event> process(Command command) throws ServiceException;
}
