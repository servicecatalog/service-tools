/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jun 27, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.events;

import org.oscm.common.interfaces.data.Callback;
import org.oscm.common.interfaces.data.DataType;
import org.oscm.common.interfaces.exceptions.ComponentException;
import org.oscm.common.interfaces.exceptions.ConnectionException;

/**
 * Interface for publishing content to other components
 * 
 * @author miethaner
 */
public interface GenericPublisher<D extends DataType> {

    /**
     * Publishes the given content to all listening components.
     * 
     * @param content
     *            the content to publish
     * @param callback
     *            the callback method to confirm the publishing
     * @throws ConnectionException
     *             if connection to topic handler failed
     */
    public void publish(D content, Callback callback) throws ComponentException;
}
