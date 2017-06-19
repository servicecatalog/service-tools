/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 19, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.ws.rs.core.Response;

import org.junit.Ignore;
import org.junit.Test;
import org.oscm.common.interfaces.exceptions.CacheException;
import org.oscm.common.interfaces.exceptions.ConcurrencyException;
import org.oscm.common.interfaces.exceptions.ConflictException;
import org.oscm.common.interfaces.exceptions.InternalException;
import org.oscm.common.interfaces.exceptions.NotFoundException;
import org.oscm.common.interfaces.exceptions.SecurityException;
import org.oscm.common.interfaces.exceptions.ServiceException;
import org.oscm.common.interfaces.exceptions.TokenException;
import org.oscm.common.interfaces.exceptions.ValidationException;
import org.oscm.common.interfaces.keys.MessageKey;
import org.oscm.common.rest.provider.ExceptionMapper;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Unit test for ExceptionMapper
 * 
 * @author miethaner
 */
public class ExceptionMapperTest {

    private static final MessageKey ERROR_KEY = new MessageKey() {

        @Override
        public String getMessage(String... values) {
            return MESSAGE;
        }

        @Override
        public Integer getCode() {
            return CODE;
        }
    };
    private static final Integer CODE = new Integer(1);
    private static final String PROPERTY = "prop";
    private static final String MESSAGE = "test";

    @Test
    public void testAllExceptionStatus() {

        testExceptionStatus(new ValidationException(ERROR_KEY, PROPERTY),
                Response.Status.BAD_REQUEST.getStatusCode());

        testExceptionStatus(new NotFoundException(ERROR_KEY),
                Response.Status.NOT_FOUND.getStatusCode());

        testExceptionStatus(new CacheException(ERROR_KEY),
                Response.Status.NOT_MODIFIED.getStatusCode());

        testExceptionStatus(new ConcurrencyException(ERROR_KEY),
                Response.Status.CONFLICT.getStatusCode());

        testExceptionStatus(new ConflictException(ERROR_KEY),
                Response.Status.CONFLICT.getStatusCode());

        testExceptionStatus(new TokenException(ERROR_KEY),
                Response.Status.UNAUTHORIZED.getStatusCode());

        testExceptionStatus(new SecurityException(ERROR_KEY),
                Response.Status.FORBIDDEN.getStatusCode());

        testExceptionStatus(new InternalException(ERROR_KEY),
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
    }

    @Test
    @Ignore
    public void testContent() {
        ExceptionMapper mapper = new ExceptionMapper();
        ValidationException e = new ValidationException(ERROR_KEY, PROPERTY);

        Response r = mapper.toResponse(e);
        assertNotNull(r.getEntity());

        Gson gson = new Gson();
        String json = gson.toJson(r.getEntity());
        JsonObject obj = gson.fromJson(json, JsonObject.class);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(),
                obj.get("status").getAsInt());
        assertEquals(36, obj.get("ref_id").getAsString().length());
        assertEquals(CODE.intValue(), obj.get("code").getAsInt());
        assertEquals(PROPERTY, obj.get("property").getAsString());
        assertEquals(MESSAGE, obj.get("message").getAsString());

    }

    public void testExceptionStatus(ServiceException e, int status) {
        ExceptionMapper mapper = new ExceptionMapper();

        Response r = mapper.toResponse(e);

        assertEquals(status, r.getStatus());
    }
}
