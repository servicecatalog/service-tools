/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 19, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.hibernate.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;
import org.oscm.common.hibernate.LocalizedString;

/**
 * Unit test for LocaleString
 * 
 * @author miethaner
 */
public class LocaleStringTest {

    @Test
    public void testFields() {

        Map<String, String> map = new TreeMap<>();

        LocalizedString ls = new LocalizedString(map);

        assertEquals(map, ls.getTranslations());

        ls = new LocalizedString();

        ls.setTranslations(map);

        assertEquals(map, ls.getTranslations());

        ls.setTranslations(null);

        assertTrue(ls.getTranslations().isEmpty());

        ls = new LocalizedString(null);

        assertTrue(ls.getTranslations().isEmpty());
    }
}
