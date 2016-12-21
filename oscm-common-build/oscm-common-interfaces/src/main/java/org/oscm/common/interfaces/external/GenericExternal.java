/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jun 30, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.external;

import org.oscm.common.interfaces.data.Callback;
import org.oscm.common.interfaces.data.DataType;
import org.oscm.common.interfaces.exceptions.ServiceException;
import org.oscm.common.interfaces.exceptions.ConnectionException;
import org.oscm.common.interfaces.exceptions.ValidationException;

/**
 * Interface for sending content to an external interface
 * 
 * @author miethaner
 */
public interface GenericExternal<D extends DataType> {

    /**
     * Sends the given content asynchronous to the given url and calls the
     * corresponding callback if resolved.
     * 
     * @param url
     *            the target url
     * @param content
     *            the content to send
     * @param success
     *            the method to be called on success
     * @param failure
     *            the method to be called on failure
     * @throws ConnectionException
     *             if connetion to external system failed
     * @throws ValidationException
     *             if url or content are not valid
     */
    public void send(String url, D content, Callback success, Callback failure)
            throws ServiceException;
}
