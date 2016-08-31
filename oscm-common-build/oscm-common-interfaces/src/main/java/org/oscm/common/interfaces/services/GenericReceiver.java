/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 30, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.services;

import org.oscm.common.interfaces.data.DataType;
import org.oscm.common.interfaces.exceptions.ComponentException;

/**
 * Interface for receiving content from internal subscriptions
 * 
 * @author miethaner
 */
public interface GenericReceiver<D extends DataType> {

    /**
     * Receives the content from an inter-component subscription.
     * 
     * @param content
     *            the content to receive
     * @throws ComponentException
     */
    public void receive(D content) throws ComponentException;
}
