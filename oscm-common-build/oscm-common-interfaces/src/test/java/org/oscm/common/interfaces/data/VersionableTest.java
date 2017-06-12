/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Dec 21, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.data;

import org.junit.Test;

/**
 * Unit tests for Versionable.
 * 
 * @author miethaner
 */
public class VersionableTest {

    public static final VersionedEntity VERSION = new VersionedEntity() {

    };

    /**
     * Call default methods for coverage
     */
    @Test
    public void testDefaultMethods() {
        VERSION.setVersion(null);
        VERSION.getVersion();
        VERSION.convertTo(null);
        VERSION.updateFrom(null);
    }

}
