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
     * Loads the configuration file from the source by the given path.
     * 
     * @param path
     *            the path to the file
     * @return the input stream of the file
     */
    public InputStream loadConfiguration();
}
