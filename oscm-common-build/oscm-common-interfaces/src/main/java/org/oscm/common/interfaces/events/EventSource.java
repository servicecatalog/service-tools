/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 13, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.events;

import java.util.List;
import java.util.UUID;

import org.oscm.common.interfaces.data.Event;

/**
 * @author miethaner
 *
 */
public interface EventSource<E extends Event> {

    public E get(UUID id);

    public List<E> getAll();
}
