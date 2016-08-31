/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 19, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.jpa.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;
import org.oscm.common.jpa.LocaleString;

/**
 * Unit test for LocaleString
 * 
 * @author miethaner
 */
public class LocaleStringTest {

    @Test
    public void testFields() {

        Map<String, String> map = new TreeMap<String, String>();

        LocaleString ls = new LocaleString(map);

        assertEquals(map, ls.getTranslations());

        ls = new LocaleString();

        ls.setTranslations(map);

        assertEquals(map, ls.getTranslations());

        ls.setTranslations(null);

        assertTrue(ls.getTranslations().isEmpty());

        ls = new LocaleString(null);

        assertTrue(ls.getTranslations().isEmpty());
    }
}
