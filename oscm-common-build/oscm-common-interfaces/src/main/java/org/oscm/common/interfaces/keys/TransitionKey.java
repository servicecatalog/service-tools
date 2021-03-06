/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 14, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.keys;

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
}
