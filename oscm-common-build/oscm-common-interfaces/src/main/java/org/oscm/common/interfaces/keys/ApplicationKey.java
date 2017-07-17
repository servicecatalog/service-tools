/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 21, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.keys;

/**
 * Interface for enums that represent application keys.
 * 
 * @author miethaner
 */
public interface ApplicationKey {

    /**
     * Enum for application types.
     * 
     * @author miethaner
     */
    public enum Type {
        INTERNAL, EXTERNAL
    }

    /**
     * Gets the application name.
     * 
     * @return the name
     */
    public String getApplicationName();

    /**
     * Gets the application type.
     * 
     * @return the type
     */
    public Type getType();
}
