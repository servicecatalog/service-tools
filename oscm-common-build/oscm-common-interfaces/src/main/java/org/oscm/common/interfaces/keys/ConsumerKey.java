/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jul 4, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.keys;

/**
 * Interface for enums that represent consumer keys.
 * 
 * @author miethaner
 */
public interface ConsumerKey {

    /**
     * Gets the consumer name.
     * 
     * @return the name
     */
    public String getConsumerName();

    /**
     * Gets the input entity.
     * 
     * @return the entity key
     */
    public EntityKey getInputEntity();
}
