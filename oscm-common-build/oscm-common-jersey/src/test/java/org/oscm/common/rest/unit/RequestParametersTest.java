/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 12, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.ws.rs.WebApplicationException;

import org.junit.Test;
import org.mockito.Mockito;
import org.oscm.common.interfaces.exceptions.ServiceException;
import org.oscm.common.interfaces.security.SecurityToken;
import org.oscm.common.rest.RequestParameters;

/**
 * Unit test for RequestParameters
 * 
 * @author miethaner
 */
public class RequestParametersTest {

    private class TestParameters extends RequestParameters {

        @Override
        public void validateParameters() throws WebApplicationException {
        }

        @Override
        public void update() {
        }
    }

    @Test
    public void testFields() throws Exception {

        SecurityToken token = Mockito.mock(SecurityToken.class);

        RequestParameters params = new TestParameters();
        params.setId(new Long(1L));
        params.setETag(new Long(1L));
        params.setLimit(new Long(1));
        params.setOffset(new Long(1L));
        params.setSecurityToken(token);

        assertEquals(new Long(1L), params.getId());
        assertEquals(new Long(1L), params.getETag());
        assertEquals(new Long(1L), params.getLimit());
        assertEquals(new Long(1L), params.getOffset());
        assertEquals(token, params.getSecurityToken());

    }

    @Test
    public void testEtagValidation() throws Exception {

        RequestParameters params = new TestParameters();

        params.setMatch("*");
        params.setNoneMatch("*");

        try {
            params.validateAndCopyETag();
            assertEquals(null, params.getETag());
        } catch (ServiceException e) {
            fail();
        }

        params = new TestParameters();
        params.setMatch("1");

        try {
            params.validateAndCopyETag();
            assertEquals(new Long(1L), params.getETag());
        } catch (ServiceException e) {
            fail();
        }

        params = new TestParameters();
        params.setNoneMatch("1");

        try {
            params.validateAndCopyETag();
            assertEquals(new Long(1L), params.getETag());
        } catch (ServiceException e) {
            fail();
        }

        params = new TestParameters();
        params.setMatch("abc");

        try {
            params.validateAndCopyETag();
            fail();
        } catch (ServiceException e) {
        }

        params = new TestParameters();
        params.setNoneMatch("abc");

        try {
            params.validateAndCopyETag();
            fail();
        } catch (ServiceException e) {
        }
    }

}
