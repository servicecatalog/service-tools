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
import org.oscm.common.interfaces.keys.VersionKey;

/**
 * Unit test for VersionKey
 * 
 * @author miethaner
 */
public class VersionKeyTest {

    private static final VersionKey key = new VersionKey() {

        @Override
        public int getMinor() {
            return MINOR;
        }

        @Override
        public int getMajor() {
            return MAJOR;
        }

        @Override
        public int getFix() {
            return FIX;
        }
    };

    private static final int MAJOR = 1;
    private static final int MINOR = 2;
    private static final int FIX = 3;

    @Test
    public void testGetCompiledVersion() {

        int v = MAJOR * 100000 + MINOR * 1000 + FIX;

        assertEquals(v, key.getCompiledVersion());
    }

    @Test
    public void testCompareVersion() {

        int result = key.compareVersion(key);

        assertEquals(0, result);

        result = key.compareVersion(MAJOR, MINOR, FIX + 1);

        assertEquals(-1, result);

        result = key.compareVersion(MAJOR, MINOR, FIX - 1);

        assertEquals(1, result);
    }

}
