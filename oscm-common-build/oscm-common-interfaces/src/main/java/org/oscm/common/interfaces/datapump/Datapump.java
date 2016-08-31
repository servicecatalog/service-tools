/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 31, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.datapump;

import org.oscm.common.interfaces.exceptions.ComponentException;

/**
 * Interface for data pump services
 * 
 * @author miethaner
 */
public interface Datapump {

    /**
     * Synchronizes the proxy entities with the corresponding components
     * persistence.
     */
    public void synchronize() throws ComponentException;
}
