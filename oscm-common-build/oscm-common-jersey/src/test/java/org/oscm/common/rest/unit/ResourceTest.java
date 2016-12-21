/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: May 19, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest.unit;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.ExtendedUriInfo;
import org.junit.Test;
import org.mockito.Mockito;
import org.oscm.common.interfaces.config.VersionKey;
import org.oscm.common.interfaces.exceptions.NotFoundException;
import org.oscm.common.interfaces.exceptions.SecurityException;
import org.oscm.common.interfaces.exceptions.ServiceException;
import org.oscm.common.interfaces.security.SecurityToken;
import org.oscm.common.rest.Backend;
import org.oscm.common.rest.Representation;
import org.oscm.common.rest.RequestParameters;
import org.oscm.common.rest.Resource;

/**
 * Unit test for Resource
 * 
 * @author miethaner
 */
public class ResourceTest extends Resource {

    public static final VersionKey VERSION_1 = new VersionKey() {

        @Override
        public int getMinor() {
            return 0;
        }

        @Override
        public int getMajor() {
            return 1;
        }

        @Override
        public String getKeyName() {
            return null;
        }

        @Override
        public int getFix() {
            return 0;
        }
    };

    private class MockRepresentation extends Representation {
        @Override
        public void validateCreate() throws WebApplicationException {
        }

        @Override
        public void validateUpdate() throws ServiceException {
        }
    }

    private class MockRequestParameters extends RequestParameters {

        @Override
        public void validateParameters() throws WebApplicationException {
        }

        @Override
        public void update() {
        }
    }

    private Backend.Get<MockRepresentation, MockRequestParameters> backendGet = new Backend.Get<MockRepresentation, MockRequestParameters>() {

        @Override
        public MockRepresentation get(MockRequestParameters params) {

            assertNotNull(params);

            return new MockRepresentation();
        }
    };

    private Backend.Post<MockRepresentation, MockRequestParameters> backendPost = new Backend.Post<MockRepresentation, MockRequestParameters>() {

        @Override
        public Long post(MockRepresentation content,
                MockRequestParameters params) {

            assertNotNull(content);
            assertNotNull(params);

            return new Long(1L);
        }
    };

    private Backend.Put<MockRepresentation, MockRequestParameters> backendPut = new Backend.Put<MockRepresentation, MockRequestParameters>() {

        @Override
        public void put(MockRepresentation content,
                MockRequestParameters params) {

            assertNotNull(content);
            assertNull(content.getETag());
            assertNotNull(params);
        }
    };

    private Backend.Put<MockRepresentation, MockRequestParameters> backendPutETag = new Backend.Put<MockRepresentation, MockRequestParameters>() {

        @Override
        public void put(MockRepresentation content,
                MockRequestParameters params) {

            assertNotNull(content);
            assertNotNull(content.getETag());
            assertNotNull(params);
        }
    };

    private Backend.Delete<MockRequestParameters> backendDelete = new Backend.Delete<MockRequestParameters>() {

        @Override
        public void delete(MockRequestParameters params) {

            assertNotNull(params);
        }
    };

    @Test
    public void testGet() throws Exception {

        MockRequestParameters params = new MockRequestParameters();
        params.setId(new Long(1L));

        ContainerRequest request = Mockito.mock(ContainerRequest.class);
        Mockito.when(request.getProperty(RequestParameters.PARAM_VERSION))
                .thenReturn(VERSION_1);

        SecurityToken token = Mockito.mock(SecurityToken.class);
        Mockito.when(request.getProperty(RequestParameters.PARAM_TOKEN))
                .thenReturn(token);

        Response response = get(request, backendGet, params, true);

        assertEquals(Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());
        assertThat(response.getEntity(), instanceOf(MockRepresentation.class));
    }

    @Test
    public void testPost() throws Exception {

        MockRepresentation content = new MockRepresentation();
        content.setId(new Long(1L));

        MockRequestParameters params = new MockRequestParameters();
        params.setId(new Long(1L));

        ContainerRequest request = Mockito.mock(ContainerRequest.class);
        Mockito.when(request.getProperty(RequestParameters.PARAM_VERSION))
                .thenReturn(VERSION_1);

        SecurityToken token = Mockito.mock(SecurityToken.class);
        Mockito.when(request.getProperty(RequestParameters.PARAM_TOKEN))
                .thenReturn(token);

        UriBuilder builder = UriBuilder.fromPath("").uri("");
        ExtendedUriInfo info = Mockito.mock(ExtendedUriInfo.class);
        Mockito.when(request.getUriInfo()).thenReturn(info);
        Mockito.when(info.getAbsolutePathBuilder()).thenReturn(builder);

        Response response = post(request, backendPost, content, params);

        assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
    }

    @Test
    public void testPut() throws Exception {

        MockRepresentation content = new MockRepresentation();
        content.setId(new Long(1L));
        content.setETag(new Long(1L));

        MockRequestParameters params = new MockRequestParameters();
        params.setId(new Long(1L));

        ContainerRequest request = Mockito.mock(ContainerRequest.class);
        Mockito.when(request.getProperty(RequestParameters.PARAM_VERSION))
                .thenReturn(VERSION_1);

        SecurityToken token = Mockito.mock(SecurityToken.class);
        Mockito.when(request.getProperty(RequestParameters.PARAM_TOKEN))
                .thenReturn(token);

        Response response = put(request, backendPut, content, params);

        assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    public void testPutWithETag() throws Exception {

        MockRepresentation content = new MockRepresentation();
        content.setId(new Long(1L));
        content.setETag(new Long(1L));

        MockRequestParameters params = new MockRequestParameters();
        params.setId(new Long(1L));
        params.setMatch("1");

        ContainerRequest request = Mockito.mock(ContainerRequest.class);
        Mockito.when(request.getProperty(RequestParameters.PARAM_VERSION))
                .thenReturn(VERSION_1);

        SecurityToken token = Mockito.mock(SecurityToken.class);
        Mockito.when(request.getProperty(RequestParameters.PARAM_TOKEN))
                .thenReturn(token);

        Response response = put(request, backendPutETag, content, params);

        assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    public void testDelete() throws Exception {

        MockRequestParameters params = new MockRequestParameters();
        params.setId(new Long(1L));

        ContainerRequest request = Mockito.mock(ContainerRequest.class);
        Mockito.when(request.getProperty(RequestParameters.PARAM_VERSION))
                .thenReturn(VERSION_1);

        SecurityToken token = Mockito.mock(SecurityToken.class);
        Mockito.when(request.getProperty(RequestParameters.PARAM_TOKEN))
                .thenReturn(token);

        Response response = delete(request, backendDelete, params);

        assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    public void testVersionIDToken() throws Exception {

        ContainerRequest requestWith = Mockito.mock(ContainerRequest.class);
        ContainerRequest requestWithoutId = Mockito
                .mock(ContainerRequest.class);
        ContainerRequest requestWithoutToken = Mockito
                .mock(ContainerRequest.class);

        SecurityToken token = Mockito.mock(SecurityToken.class);

        Mockito.when(requestWith.getProperty(RequestParameters.PARAM_VERSION))
                .thenReturn(VERSION_1);
        Mockito.when(requestWithoutToken
                .getProperty(RequestParameters.PARAM_VERSION))
                .thenReturn(VERSION_1);
        Mockito.when(requestWith.getProperty(RequestParameters.PARAM_TOKEN))
                .thenReturn(token);

        MockRequestParameters params = new MockRequestParameters();

        try {
            get(requestWithoutId, backendGet, params, false);
            fail();
        } catch (NotFoundException e) {
        }

        try {
            post(requestWithoutId, backendPost, null, params);
            fail();
        } catch (NotFoundException e) {
        }

        try {
            put(requestWithoutId, backendPut, null, params);
            fail();
        } catch (NotFoundException e) {
        }

        try {
            delete(requestWithoutId, backendDelete, params);
            fail();
        } catch (NotFoundException e) {
        }

        try {
            get(requestWithoutToken, backendGet, params, false);
            fail();
        } catch (SecurityException e) {
        }

        try {
            post(requestWithoutToken, backendPost, null, params);
            fail();
        } catch (SecurityException e) {
        }

        try {
            put(requestWithoutToken, backendPut, null, params);
            fail();
        } catch (SecurityException e) {
        }

        try {
            delete(requestWithoutToken, backendDelete, params);
            fail();
        } catch (SecurityException e) {
        }

        try {
            get(requestWith, backendGet, params, true);
            fail();
        } catch (NotFoundException e) {
        }

        try {
            put(requestWith, backendPut, null, params);
            fail();
        } catch (NotFoundException e) {
        }

        try {
            delete(requestWith, backendDelete, params);
            fail();
        } catch (NotFoundException e) {
        }
    }
}
