/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 14, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.keys;

import org.oscm.common.interfaces.data.Event;

/**
 * Interface for enums that represent transition keys.
 * 
 * @author miethaner
 */
public interface TransitionKey {

    /**
     * Gets the transition name.
     * 
     * @return the name
     */
    public String getTransitionName();

    /**
     * Gets the input event class.
     * 
     * @return the event class
     */
    public Class<? extends Event> getInputClass();

    /**
     * Gets the output event class.
     * 
     * @return the event class
     */
    public Class<? extends Event> getOutputClass();
}
