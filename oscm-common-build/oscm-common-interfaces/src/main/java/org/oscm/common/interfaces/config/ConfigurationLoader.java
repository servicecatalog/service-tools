/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jan 20, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.config;

import java.io.InputStream;

/**
 * Interface for configuration loader classes.
 * 
 * @author miethaner
 */
public interface ConfigurationLoader {

    /**
     * Loads the configuration from the source.
     * 
     * @return the input stream of the configuration
     */
    public InputStream loadConfiguration();
}
