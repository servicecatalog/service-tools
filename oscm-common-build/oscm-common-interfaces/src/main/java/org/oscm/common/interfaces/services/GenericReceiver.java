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
import org.oscm.common.interfaces.exceptions.InternalException;
import org.oscm.common.interfaces.exceptions.ValidationException;

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
     * @throws ValidationException
     *             if parameters are not valid
     * @throws InternalException
     *             if an unexpected error occurs
     */
    public void receive(D content) throws ComponentException;
}
