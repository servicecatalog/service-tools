/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 6, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.keys;

import org.oscm.common.interfaces.data.Event;

/**
 * Interface for enums that represent activity keys.
 * 
 * @author miethaner
 */
public interface ActivityKey {

    /**
     * Enum for activity types.
     * 
     * @author miethaner
     */
    public enum Type {
        COMMAND, QUERY, UPDATE, NONE
    }

    /**
     * Gets the activity name.
     * 
     * @return the name
     */
    public String getActivityName();

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

    /**
     * Gets the activity type.
     * 
     * @return the type
     */
    public Type getType();

    /**
     * Gets the version since this activity is available.
     * 
     * @return the version
     */
    public VersionKey getSince();

    /**
     * Gets the version until this activity is available.
     * 
     * @return the version
     */
    public VersionKey getUntil();
}