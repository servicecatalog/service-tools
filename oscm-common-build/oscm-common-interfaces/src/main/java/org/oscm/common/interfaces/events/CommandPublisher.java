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
 * 
 * @author miethaner
 */
public interface CommandPublisher {

    public static final String SERVICE_PUBLISHER = "cmd_publisher";

    public void publish(Command command, ResultHandler handler)
            throws ServiceException;
}
