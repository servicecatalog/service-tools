/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 28, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.keys;

import org.oscm.common.interfaces.data.Event;

/**
 * Interface for enums that represent entity keys.
 * 
 * @author miethaner
 */
public interface EntityKey {

    /**
     * Gets the entity name.
     * 
     * @return the name
     */
    public String getEntityName();

    /**
     * Gets the event class for this entity.
     * 
     * @return the event class
     */
    public Class<? extends Event> getEntityClass();

    /**
     * Gets the application for this entity.
     * 
     * @return the application key
     */
    public ApplicationKey getApplication();
}
