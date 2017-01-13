/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 25, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.kafka.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.oscm.common.interfaces.config.ServiceKey;
import org.oscm.common.kafka.InternalToken;

/**
 * Unit test for Token
 * 
 * @author miethaner
 */
public class InternalTokenTest {

    @Test
    public void testFields() {

        InternalToken test = new InternalToken();

        assertEquals(new Long(0), test.getUserId());
        assertEquals(new Long(0), test.getOrganizationId());
        assertEquals(new Long(0), test.getTenantId());
        assertTrue(test.getRoles().contains(ServiceKey.PRIVATE_ROLE));
    }
}
