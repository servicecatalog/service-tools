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
import org.oscm.common.interfaces.enums.Operation;

/**
 * Unit test for common enums
 * 
 * @author miethaner
 */
public class EnumTest {

    @Test
    public void testMessages() {

        assertEquals("Debug", Messages.DEBUG.message());
        assertEquals(new Integer(0), Messages.DEBUG.error());
    }

    @Test
    public void testOperation() {

        assertEquals("CREATED", Operation.CREATED.toString());
        assertEquals("UPDATED", Operation.UPDATED.toString());
        assertEquals("DELETED", Operation.DELETED.toString());
        assertEquals("SUSPENDED", Operation.SUSPENDED.toString());
    }
}
