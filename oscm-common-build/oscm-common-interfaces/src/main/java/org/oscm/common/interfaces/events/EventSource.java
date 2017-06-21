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
 * Interface for retrieving events from a source.
 * 
 * @param E
 *            the event subclass
 * 
 * @author miethaner
 */
public interface EventSource<E extends Event> {

    /**
     * Gets the event with given id from the source. Returns null, if no event
     * with the id does exist.
     * 
     * @param id
     *            the event id
     * @return the event or null
     */
    public E get(UUID id);

    /**
     * Gets all events from the source.
     * 
     * @return the list of events
     */
    public List<E> getAll();
}
