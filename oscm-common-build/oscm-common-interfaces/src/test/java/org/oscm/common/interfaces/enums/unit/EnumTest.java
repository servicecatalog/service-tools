/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 18, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.enums.unit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.oscm.common.interfaces.enums.Messages;

/**
 * Unit test for common enums
 * 
 * @author miethaner
 */
public class EnumTest {

    @Test
    public void testMessages() {

        assertEquals("DEBUG: TEST", Messages.DEBUG.getMessage("TEST"));
        assertEquals(new Integer(0), Messages.DEBUG.getCode());
    }
}
