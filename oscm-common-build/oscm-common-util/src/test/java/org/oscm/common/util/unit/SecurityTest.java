/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 19, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.util.unit;

import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;
import org.mockito.Mockito;
import org.oscm.common.interfaces.config.ConfigurationImporter;
import org.oscm.common.interfaces.config.ConfigurationKey;
import org.oscm.common.interfaces.config.ServiceKey;
import org.oscm.common.interfaces.config.VersionKey;
import org.oscm.common.interfaces.exceptions.SecurityException;
import org.oscm.common.interfaces.security.SecurityToken;
import org.oscm.common.util.ServiceConfiguration;
import org.oscm.common.util.validators.Security;

/**
 * Unit test for Security
 * 
 * @author miethaner
 */
public class SecurityTest {

    private static final ServiceKey key = new ServiceKey() {

        @Override
        public String getKeyName() {
            return "key";
        }

        @Override
        public String getDefaultRole() {
            return ServiceKey.PRIVATE_ROLE;
        }
    };

    private class Importer implements ConfigurationImporter {

        private Set<String> roles;

        public Importer(Set<String> roles) {
            this.roles = roles;
        }

        @Override
        public Map<ServiceKey, Set<String>> readRoles(ServiceKey[] keys) {
            Map<ServiceKey, Set<String>> map = new HashMap<>();
            map.put(key, roles);

            return map;
        }

        @Override
        public Map<ConfigurationKey, String> readEntries(
                ConfigurationKey[] keys) {
            return Collections.emptyMap();
        }

    }

    @Test
    public void testSecurityPositive() throws Exception {

        Importer importer = new Importer(
                new HashSet<>(Arrays.asList(ServiceKey.PUBLIC_ROLE)));
        ServiceConfiguration.init(importer, new VersionKey[] {},
                new ServiceKey[] { key }, new ConfigurationKey[] {});

        SecurityToken token = Mockito.mock(SecurityToken.class);

        Security.validatePermission(key, token);

        importer = new Importer(
                new HashSet<>(Arrays.asList(ServiceKey.PRIVATE_ROLE)));
        ServiceConfiguration.init(importer, new VersionKey[] {},
                new ServiceKey[] { key }, new ConfigurationKey[] {});

        Set<String> roles = new TreeSet<>();
        roles.add(ServiceKey.PRIVATE_ROLE);
        Mockito.when(token.getRoles()).thenReturn(roles);

        Security.validatePermission(key, token);
    }

    @Test
    public void testSecurityNegative() throws Exception {

        Importer importer = new Importer(
                new HashSet<>(Arrays.asList(ServiceKey.PRIVATE_ROLE)));
        ServiceConfiguration.init(importer, new VersionKey[] {},
                new ServiceKey[] { key }, new ConfigurationKey[] {});

        try {
            Security.validatePermission(key, null);
            fail();
        } catch (SecurityException e) {
        }

        Set<String> roles1 = new TreeSet<>();
        roles1.add(ServiceKey.PUBLIC_ROLE);

        SecurityToken token = Mockito.mock(SecurityToken.class);
        Mockito.when(token.getRoles()).thenReturn(roles1);

        try {
            Security.validatePermission(key, token);
            fail();
        } catch (SecurityException e) {
        }

        Mockito.when(token.getRoles()).thenReturn(null);

        try {
            Security.validatePermission(key, token);
            fail();
        } catch (SecurityException e) {
        }
    }

}
