/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Dec 21, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.config;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.oscm.common.interfaces.data.Version;

/**
 * Unit test for VersionKey
 * 
 * @author miethaner
 */
public class VersionKeyTest {

    private static final int MAJOR = 1;
    private static final int MINOR = 2;
    private static final int FIX = 3;

    @Test
    public void testGetCompiledVersion() {

        Version version = new Version(MAJOR, MINOR, FIX);

        int v = MAJOR * 100000 + MINOR * 1000 + FIX;

        assertEquals(v, version.getCompiledVersion());
    }

    @Test
    public void testCompareVersion() {

        Version version = new Version(MAJOR, MINOR, FIX);

        int result = version.compare(version);

        assertEquals(0, result);

        result = version.compare(MAJOR, MINOR, FIX + 1);

        assertEquals(-1, result);

        result = version.compare(MAJOR, MINOR, FIX - 1);

        assertEquals(1, result);
    }

}
