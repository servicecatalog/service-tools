/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jun 30, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.external;

import org.oscm.common.interfaces.data.DataType;
import org.oscm.common.interfaces.exceptions.ComponentException;
import org.oscm.common.interfaces.exceptions.ConnectionException;
import org.oscm.common.interfaces.exceptions.ValidationException;

/**
 * Interface for sending content to an external interface
 * 
 * @author miethaner
 */
public interface GenericExternal<D extends DataType> {

    /**
     * Sends the given content to the given url
     * 
     * @param url
     *            the target url
     * @param content
     *            the content to send
     * @throws ConnectionException
     *             if connetion to external system failed
     * @throws ValidationException
     *             if url or content are not valid
     */
    public void send(String url, D content) throws ComponentException;
}
