/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: May 19, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.ExtendedUriInfo;
import org.junit.Test;
import org.mockito.Mockito;
import org.oscm.common.interfaces.config.ConfigurationImporter;
import org.oscm.common.interfaces.config.ConfigurationKey;
import org.oscm.common.interfaces.config.ServiceKey;
import org.oscm.common.interfaces.config.VersionKey;
import org.oscm.common.rest.RequestParameters;
import org.oscm.common.rest.Since;
import org.oscm.common.rest.Until;
import org.oscm.common.rest.VersionFilter;
import org.oscm.common.util.ServiceConfiguration;

/**
 * Unit test for VersionFilter
 * 
 * @author miethaner
 */
public class VersionFilterTest {

    private static final int API_VERSION_1 = 1;
    private static final VersionKey versionKey = new VersionKey() {

        @Override
        public String getKeyName() {
            return "";
        }

        @Override
        public int getMajor() {
            return API_VERSION_1;
        }

        @Override
        public int getMinor() {
            return 0;
        }

        @Override
        public int getFix() {
            return 0;
        }
    };

    public interface MockMultivaluedMap extends MultivaluedMap<String, String> {
    }

    private class Importer implements ConfigurationImporter {

        @Override
        public Map<String, Set<String>> readRoles() {
            return Collections.emptyMap();
        }

        @Override
        public Map<String, String> readEntries() {
            return Collections.emptyMap();
        }

    }

    @SuppressWarnings({ "boxing" })
    private void testVersionFilter(String version, Method method)
            throws WebApplicationException {

        Importer importer = new Importer();
        ServiceConfiguration.init(importer, new VersionKey[] { versionKey },
                new ServiceKey[] {}, new ConfigurationKey[] {});

        ResourceInfo resourceInfo = Mockito.mock(ResourceInfo.class);

        Mockito.when(resourceInfo.getResourceMethod()).thenReturn(method);

        ContainerRequest request = Mockito.mock(ContainerRequest.class);
        ExtendedUriInfo info = Mockito.mock(ExtendedUriInfo.class);
        MultivaluedMap<String, String> map = Mockito
                .mock(MockMultivaluedMap.class);

        Mockito.when(request.getUriInfo()).thenReturn(info);
        Mockito.when(info.getPathParameters()).thenReturn(map);

        List<String> list = new ArrayList<>();
        list.add(version);
        Mockito.when(map.containsKey(RequestParameters.PARAM_VERSION))
                .thenReturn(true);
        Mockito.when(map.get(RequestParameters.PARAM_VERSION)).thenReturn(list);
        Mockito.when(info.getPathParameters()).thenReturn(map);

        VersionFilter filter = new VersionFilter();
        filter.setResourceInfo(resourceInfo);
        filter.filter(request);

        Mockito.verify(request).setProperty(RequestParameters.PARAM_VERSION,
                versionKey);

    }

    public class MockClass {

        public void mockMethod() {
        }
    }

    @Test
    public void testVersionFilterVersionPositive() {

        String version = "v" + API_VERSION_1 * 100000;

        try {
            Method method = MockClass.class.getMethod("mockMethod");

            testVersionFilter(version, method);
        } catch (WebApplicationException | NoSuchMethodException
                | SecurityException e) {
            fail();
        }
    }

    @Test
    public void testPathParamNegative() {

        ContainerRequest request = Mockito.mock(ContainerRequest.class);
        ExtendedUriInfo info = Mockito.mock(ExtendedUriInfo.class);
        MultivaluedMap<String, String> map = Mockito
                .mock(MockMultivaluedMap.class);

        Mockito.when(request.getUriInfo()).thenReturn(info);
        Mockito.when(info.getPathParameters()).thenReturn(map);

        VersionFilter filter = new VersionFilter();

        try {
            filter.filter(request);
        } catch (WebApplicationException e) {
            assertEquals(Status.NOT_FOUND.getStatusCode(),
                    e.getResponse().getStatus());
        }
    }

    @Test
    public void testVersionFilterVersionNegativePattern() {

        String version = "n42";

        try {
            Method method = MockClass.class.getMethod("mockMethod");

            testVersionFilter(version, method);
            fail();
        } catch (WebApplicationException e) {
            assertEquals(Status.NOT_FOUND.getStatusCode(),
                    e.getResponse().getStatus());
        } catch (NoSuchMethodException | SecurityException e) {
            fail();
        }
    }

    @Test
    public void testVersionFilterVersionNegativeNull() {

        try {
            Method method = MockClass.class.getMethod("mockMethod");

            testVersionFilter(null, method);
            fail();
        } catch (WebApplicationException e) {
            assertEquals(Status.NOT_FOUND.getStatusCode(),
                    e.getResponse().getStatus());
        } catch (NoSuchMethodException | SecurityException e) {
            fail();
        }
    }

    @Test
    public void testVersionFilterVersionNegativeNumber() {

        String version = "v42";

        try {
            Method method = MockClass.class.getMethod("mockMethod");

            testVersionFilter(version, method);
            fail();
        } catch (WebApplicationException e) {
            assertEquals(Status.NOT_FOUND.getStatusCode(),
                    e.getResponse().getStatus());
        } catch (NoSuchMethodException | SecurityException e) {
            fail();
        }
    }

    public class MockClassSincePositive {

        @Since(major = API_VERSION_1)
        public void mockMethod() {
        }
    }

    @Test
    public void testVersionFilterSincePositive() throws Exception {

        String version = "v" + API_VERSION_1 * 100000;

        Method method = MockClassSincePositive.class.getMethod("mockMethod");

        testVersionFilter(version, method);
    }

    public class MockClassSinceNegative {

        @Since(major = API_VERSION_1 + 1)
        public void mockMethod() {
        }
    }

    @Test
    public void testVersionFilterSinceNegative() {

        String version = "v" + API_VERSION_1 * 100000;

        try {
            Method method = MockClassSinceNegative.class
                    .getMethod("mockMethod");
            testVersionFilter(version, method);
            fail();
        } catch (WebApplicationException e) {
            assertEquals(Status.NOT_FOUND.getStatusCode(),
                    e.getResponse().getStatus());
        } catch (NoSuchMethodException | SecurityException e) {
            fail();
        }
    }

    public class MockClassUntilPositive {

        @Until(major = API_VERSION_1 + 1)
        public void mockMethod() {
        }
    }

    @Test
    public void testVersionFilterUntilPositive() {

        String version = "v" + API_VERSION_1 * 100000;

        try {
            Method method = MockClassUntilPositive.class
                    .getMethod("mockMethod");
            testVersionFilter(version, method);
        } catch (WebApplicationException | NoSuchMethodException
                | SecurityException e) {
            fail();
        }

    }

    public class MockClassUntilNegative {

        @Until(major = API_VERSION_1)
        public void mockMethod() {
        }
    }

    @Test
    public void testVersionFilterUntilNegative() {

        String version = "v" + API_VERSION_1 * 100000;

        try {
            Method method = MockClassUntilNegative.class
                    .getMethod("mockMethod");
            testVersionFilter(version, method);
            fail();
        } catch (WebApplicationException e) {
            assertEquals(Status.NOT_FOUND.getStatusCode(),
                    e.getResponse().getStatus());
        } catch (NoSuchMethodException | SecurityException e) {
            fail();
        }
    }
}
