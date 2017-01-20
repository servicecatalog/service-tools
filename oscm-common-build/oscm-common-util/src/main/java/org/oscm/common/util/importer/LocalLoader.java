/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jan 20, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.util.importer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.oscm.common.interfaces.config.ConfigurationLoader;

/**
 * Loader class for local sources.
 * 
 * @author miethaner
 */
public class LocalLoader implements ConfigurationLoader {

    private String path;

    public LocalLoader(String path) {
        this.path = path;
    }

    @Override
    public InputStream loadConfiguration() {
        try {
            return new FileInputStream(new File(path));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Unable to find configuration file");
        }
    }

}
