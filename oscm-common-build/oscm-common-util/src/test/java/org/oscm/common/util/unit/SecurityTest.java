/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 19, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.util.unit;

import static org.junit.Assert.fail;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;
import org.mockito.Mockito;
import org.oscm.common.interfaces.config.GenericConfig;
import org.oscm.common.interfaces.exceptions.SecurityException;
import org.oscm.common.interfaces.security.SecurityToken;
import org.oscm.common.util.Security;

/**
 * Unit test for Security
 * 
 * @author miethaner
 */
public class SecurityTest {

    private class TestConfig implements GenericConfig<String, String> {

        @Override
        public String getConfig(String config) {
            return null;
        }

        @Override
        public Map<String, String> getAllConfigs() {
            return null;
        }

        @Override
        public boolean isServiceRestricted(String service) {
            return false;
        }

        @Override
        public Set<String> getRolesForService(String service) {
            return null;
        }
    }

    @SuppressWarnings("boxing")
    @Test
    public void testSecurityPositive() throws Exception {

        TestConfig config = Mockito.spy(new TestConfig());
        SecurityToken token = Mockito.mock(SecurityToken.class);

        Security.validatePermission(null, "test", token);

        Security.validatePermission(config, "test", token);

        Set<String> roles = new TreeSet<String>();
        roles.add("role");

        Mockito.when(config.isServiceRestricted(Mockito.anyString()))
                .thenReturn(true);
        Mockito.when(config.getRolesForService(Mockito.anyString()))
                .thenReturn(roles);
        Mockito.when(token.getRoles()).thenReturn(roles);

        Security.validatePermission(config, "test", token);
    }

    @SuppressWarnings("boxing")
    @Test
    public void testSecurityNegative() throws Exception {

        TestConfig config = Mockito.spy(new TestConfig());
        SecurityToken token = Mockito.mock(SecurityToken.class);

        Mockito.when(config.isServiceRestricted(Mockito.anyString()))
                .thenReturn(true);

        try {
            Security.validatePermission(config, "test", null);
            fail();
        } catch (SecurityException e) {
        }

        Set<String> roles1 = new TreeSet<String>();
        roles1.add("role1");

        Set<String> roles2 = new TreeSet<String>();
        roles2.add("role2");

        try {
            Security.validatePermission(config, "test", token);
            fail();
        } catch (SecurityException e) {
        }

        Mockito.when(config.getRolesForService(Mockito.anyString()))
                .thenReturn(roles1);
        Mockito.when(token.getRoles()).thenReturn(roles2);

        try {
            Security.validatePermission(config, "test", token);
            fail();
        } catch (SecurityException e) {
        }
    }

}
