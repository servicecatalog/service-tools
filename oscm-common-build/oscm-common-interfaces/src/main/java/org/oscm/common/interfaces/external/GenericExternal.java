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
     * @throws ComponentException
     */
    public void send(String url, D content) throws ComponentException;
}
