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
import org.oscm.common.interfaces.exceptions.ServiceException;

/**
 * @author miethaner
 *
 */
@FunctionalInterface
public interface QueryService {

    public static final String SERVICE_QUERY = "query_service";

    public List<Event> query(Event event, Token token) throws ServiceException;
}
