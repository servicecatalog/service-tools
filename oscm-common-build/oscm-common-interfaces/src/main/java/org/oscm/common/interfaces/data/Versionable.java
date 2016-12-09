/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Dec 6, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.data;

/**
 * Default interface for versionable representations.
 * 
 * @author miethaner
 */
public interface Versionable {

    /**
     * Updates the fields and format of the internal version to the current one
     */
    default public void update() {
        // nothing to update
    }

    /**
     * Converts the format and fields of the current version to the internal old
     * one
     */
    default public void convert() {
        // nothing to convert
    }

}
