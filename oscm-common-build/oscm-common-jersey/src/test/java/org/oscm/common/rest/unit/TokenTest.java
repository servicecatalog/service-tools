/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 25, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import org.junit.Test;
import org.oscm.common.interfaces.data.Token;

/**
 * Unit test for Token
 * 
 * @author miethaner
 */
public class TokenTest {

    @Test
    public void testFields() {

        String uuid = "8e557f4a-d808-44f4-a846-d228e2c9691a";

        Set<String> roles = new TreeSet<>();
        roles.add("test");
        Token test = new Token();
        test.setUserId(UUID.fromString(uuid));
        test.setOrganizationId(UUID.fromString(uuid));
        test.setTenantId(UUID.fromString(uuid));
        test.setRoles(roles);

        assertEquals(UUID.fromString(uuid), test.getUserId());
        assertEquals(UUID.fromString(uuid), test.getOrganizationId());
        assertEquals(UUID.fromString(uuid), test.getTenantId());
        assertTrue(test.getRoles().contains("test"));

        test = new Token();
        test.setUserId((UUID) null);
        test.setOrganizationId((UUID) null);
        test.setTenantId((UUID) null);
        test.setRoles((Set<String>) null);

        assertNull(test.getUserId());
        assertNull(test.getOrganizationId());
        assertNull(test.getTenantId());
        assertTrue(test.getRoles().isEmpty());
    }
}
