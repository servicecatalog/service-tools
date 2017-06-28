/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 6, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.keys;

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
     * Gets the input entity.
     * 
     * @return the entity key
     */
    public EntityKey getInputEntity();

    /**
     * Gets the output entity.
     * 
     * @return the entity key
     */
    public EntityKey getOutputEntity();

    /**
     * Gets the application which executes this activity.
     * 
     * @return the application key
     */
    public ApplicationKey getApplication();

    /**
     * Gets the activity type.
     * 
     * @return the type
     */
    public Type getType();

    /**
     * Gets the version since this activity is available.
     * 
     * @return the version key
     */
    public VersionKey getSince();

    /**
     * Gets the version until this activity is available.
     * 
     * @return the version key
     */
    public VersionKey getUntil();
}
