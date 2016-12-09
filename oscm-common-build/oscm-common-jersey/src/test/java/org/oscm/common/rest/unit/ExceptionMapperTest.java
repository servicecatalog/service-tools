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

import org.junit.Test;
import org.oscm.common.interfaces.exceptions.CacheException;
import org.oscm.common.interfaces.exceptions.ComponentException;
import org.oscm.common.interfaces.exceptions.ConcurrencyException;
import org.oscm.common.interfaces.exceptions.ConflictException;
import org.oscm.common.interfaces.exceptions.InternalException;
import org.oscm.common.interfaces.exceptions.NotFoundException;
import org.oscm.common.interfaces.exceptions.SecurityException;
import org.oscm.common.interfaces.exceptions.TokenException;
import org.oscm.common.interfaces.exceptions.ValidationException;
import org.oscm.common.rest.ExceptionMapper;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Unit test for ExceptionMapper
 * 
 * @author miethaner
 */
public class ExceptionMapperTest {

    private static final Integer ERROR = new Integer(1);
    private static final String PROPERTY = "prop";
    private static final String MESSAGE = "test";
    private static final String MORE_INFO = "more";

    @Test
    public void testAllExceptionStatus() {

        testExceptionStatus(new ValidationException(ERROR, MESSAGE),
                Response.Status.BAD_REQUEST.getStatusCode());

        testExceptionStatus(new NotFoundException(ERROR, MESSAGE),
                Response.Status.NOT_FOUND.getStatusCode());

        testExceptionStatus(new CacheException(ERROR, MESSAGE),
                Response.Status.NOT_MODIFIED.getStatusCode());

        testExceptionStatus(new ConcurrencyException(ERROR, MESSAGE),
                Response.Status.CONFLICT.getStatusCode());

        testExceptionStatus(new ConflictException(ERROR, MESSAGE),
                Response.Status.CONFLICT.getStatusCode());

        testExceptionStatus(new TokenException(ERROR, MESSAGE),
                Response.Status.UNAUTHORIZED.getStatusCode());

        testExceptionStatus(new SecurityException(ERROR, MESSAGE),
                Response.Status.FORBIDDEN.getStatusCode());

        testExceptionStatus(new InternalException(ERROR, MESSAGE),
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
    }

    @Test
    public void testContent() {
        ExceptionMapper mapper = new ExceptionMapper();
        ValidationException e = new ValidationException(ERROR, PROPERTY,
                MESSAGE, MORE_INFO);

        Response r = mapper.toResponse(e);
        assertNotNull(r.getEntity());

        Gson gson = new Gson();
        String json = gson.toJson(r.getEntity());
        JsonObject obj = gson.fromJson(json, JsonObject.class);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(),
                obj.get("code").getAsInt());
        assertEquals(ERROR.intValue(), obj.get("error").getAsInt());
        assertEquals(PROPERTY, obj.get("property").getAsString());
        assertEquals(MESSAGE, obj.get("message").getAsString());
        assertEquals(MORE_INFO, obj.get("more_info").getAsString());

    }

    public void testExceptionStatus(ComponentException e, int status) {
        ExceptionMapper mapper = new ExceptionMapper();

        Response r = mapper.toResponse(e);

        assertEquals(status, r.getStatus());
    }
}
