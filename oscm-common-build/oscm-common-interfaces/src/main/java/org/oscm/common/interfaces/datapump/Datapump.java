/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 31, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.datapump;

import org.oscm.common.interfaces.exceptions.ComponentException;
import org.oscm.common.interfaces.exceptions.ConnectionException;
import org.oscm.common.interfaces.exceptions.InternalException;

/**
 * Interface for data pump services
 * 
 * @author miethaner
 */
public interface Datapump {

    /**
     * Synchronizes the proxy entities with the corresponding components
     * persistence.
     * 
     * @throws ConnectionException
     *             if connection to foreign datasource failed
     * @throws InternalException
     *             if data could not converted or persisted
     */
    public void synchronize() throws ComponentException;
}
