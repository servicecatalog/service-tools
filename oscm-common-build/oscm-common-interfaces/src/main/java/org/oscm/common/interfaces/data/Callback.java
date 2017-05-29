/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Dec 15, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.data;

import org.oscm.common.interfaces.exceptions.ServiceException;

/**
 * Generic callback interface.
 * 
 * @author miethaner
 */
public interface Callback {

    /**
     * Callback method with service exceptions.
     * 
     * @throws ServiceException
     */
    public void callback() throws ServiceException;
}
