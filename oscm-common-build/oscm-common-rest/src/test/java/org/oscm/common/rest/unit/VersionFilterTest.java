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
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.ExtendedUriInfo;
import org.junit.Test;
import org.mockito.Mockito;
import org.oscm.common.rest.RequestParameters;
import org.oscm.common.rest.Since;
import org.oscm.common.rest.Until;
import org.oscm.common.rest.VersionFilter;

/**
 * Unit test for VersionFilter
 * 
 * @author miethaner
 */
public class VersionFilterTest {

    private static final int API_VERSION_1 = 1;
    private static final int[] API_VERSIONS = { API_VERSION_1 };

    public interface MockMultivaluedMap extends MultivaluedMap<String, String> {
    }

    @SuppressWarnings({ "boxing" })
    private void testVersionFilter(String version, Method method)
            throws WebApplicationException {

        VersionFilter.setApiVersions(API_VERSIONS);

        ResourceInfo resourceInfo = Mockito.mock(ResourceInfo.class);

        Mockito.when(resourceInfo.getResourceMethod()).thenReturn(method);

        ContainerRequest request = Mockito.mock(ContainerRequest.class);
        ExtendedUriInfo info = Mockito.mock(ExtendedUriInfo.class);
        MultivaluedMap<String, String> map = Mockito
                .mock(MockMultivaluedMap.class);

        Mockito.when(request.getUriInfo()).thenReturn(info);
        Mockito.when(info.getPathParameters()).thenReturn(map);

        List<String> list = new ArrayList<String>();
        list.add(version);
        Mockito.when(map.containsKey(RequestParameters.PARAM_VERSION))
                .thenReturn(true);
        Mockito.when(map.get(RequestParameters.PARAM_VERSION)).thenReturn(list);
        Mockito.when(info.getPathParameters()).thenReturn(map);

        VersionFilter filter = new VersionFilter();
        filter.setResourceInfo(resourceInfo);
        filter.filter(request);

        Mockito.verify(request).setProperty(RequestParameters.PARAM_VERSION,
                new Integer(version.substring(VersionFilter.OFFSET_VERSION)));

    }

    public class MockClass {

        public void mockMethod() {
        }
    }

    @Test
    public void testVersionFilterVersionPositive() {

        String version = "v" + API_VERSION_1;

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
            assertEquals(Status.NOT_FOUND.getStatusCode(), e.getResponse()
                    .getStatus());
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
            assertEquals(Status.NOT_FOUND.getStatusCode(), e.getResponse()
                    .getStatus());
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
            assertEquals(Status.NOT_FOUND.getStatusCode(), e.getResponse()
                    .getStatus());
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
            assertEquals(Status.NOT_FOUND.getStatusCode(), e.getResponse()
                    .getStatus());
        } catch (NoSuchMethodException | SecurityException e) {
            fail();
        }
    }

    public class MockClassSincePositive {

        @Since(API_VERSION_1)
        public void mockMethod() {
        }
    }

    @Test
    public void testVersionFilterSincePositive() {

        String version = "v" + API_VERSION_1;

        try {
            Method method = MockClassSincePositive.class
                    .getMethod("mockMethod");

            testVersionFilter(version, method);
        } catch (WebApplicationException | NoSuchMethodException
                | SecurityException e) {
            fail();
        }
    }

    public class MockClassSinceNegative {

        @Since(API_VERSION_1 + 1)
        public void mockMethod() {
        }
    }

    @Test
    public void testVersionFilterSinceNegative() {

        String version = "v" + API_VERSION_1;

        try {
            Method method = MockClassSinceNegative.class
                    .getMethod("mockMethod");
            testVersionFilter(version, method);
            fail();
        } catch (WebApplicationException e) {
            assertEquals(Status.NOT_FOUND.getStatusCode(), e.getResponse()
                    .getStatus());
        } catch (NoSuchMethodException | SecurityException e) {
            fail();
        }
    }

    public class MockClassUntilPositive {

        @Until(API_VERSION_1 + 1)
        public void mockMethod() {
        }
    }

    @Test
    public void testVersionFilterUntilPositive() {

        String version = "v" + API_VERSION_1;

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

        @Until(API_VERSION_1)
        public void mockMethod() {
        }
    }

    @Test
    public void testVersionFilterUntilNegative() {

        String version = "v" + API_VERSION_1;

        try {
            Method method = MockClassUntilNegative.class
                    .getMethod("mockMethod");
            testVersionFilter(version, method);
            fail();
        } catch (WebApplicationException e) {
            assertEquals(Status.NOT_FOUND.getStatusCode(), e.getResponse()
                    .getStatus());
        } catch (NoSuchMethodException | SecurityException e) {
            fail();
        }
    }
}
