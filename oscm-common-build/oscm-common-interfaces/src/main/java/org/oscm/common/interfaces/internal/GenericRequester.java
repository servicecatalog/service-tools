/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 18, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.internal;

import org.oscm.common.interfaces.data.DataType;
import org.oscm.common.interfaces.exceptions.ComponentException;
import org.oscm.common.interfaces.exceptions.ConnectionException;

/**
 * Interface for sending requests to other components
 * 
 * @author miethaner
 */
public interface GenericRequester<D extends DataType> {

    /**
     * Sends a request with the given content to the corresponding component
     * 
     * @param content
     *            the content to send
     * @throws ConnectionException
     *             if connection to queue handler failed
     */
    public void request(D content) throws ComponentException;
}
