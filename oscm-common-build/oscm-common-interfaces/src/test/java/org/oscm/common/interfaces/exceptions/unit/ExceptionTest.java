/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 18, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.exceptions.unit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.oscm.common.interfaces.config.ErrorKey;
import org.oscm.common.interfaces.exceptions.CacheException;
import org.oscm.common.interfaces.exceptions.ConcurrencyException;
import org.oscm.common.interfaces.exceptions.ConflictException;
import org.oscm.common.interfaces.exceptions.ConnectionException;
import org.oscm.common.interfaces.exceptions.InternalException;
import org.oscm.common.interfaces.exceptions.NotFoundException;
import org.oscm.common.interfaces.exceptions.SecurityException;
import org.oscm.common.interfaces.exceptions.ServiceException;
import org.oscm.common.interfaces.exceptions.TokenException;
import org.oscm.common.interfaces.exceptions.ValidationException;

/**
 * Unit tests for the common exceptions
 * 
 * @author miethaner
 */
public class ExceptionTest {

    private static final ErrorKey KEY = new ErrorKey() {

        @Override
        public String getMessage(String... values) {
            return TEST;
        }

        @Override
        public String getKeyName() {
            return "KEY";
        }

        @Override
        public Integer getId() {
            return ONE;
        }
    };
    private static final String TEST = "test";
    private static final String CAUSE = "cause";
    private static final Integer ONE = new Integer(1);
    private static final Integer TWO = new Integer(2);

    private Exception exception = new Exception(CAUSE);

    @Test
    public void testServiceExcpetion() {

        ServiceException e = new ServiceException(KEY);
        validateException(e, ONE, TEST, null);

        e = new ServiceException(KEY, exception);
        validateException(e, ONE, TEST, exception);

        e = new ServiceException(KEY, exception);
        e.setError(TWO);
        validateException(e, TWO, TEST, exception);
    }

    @Test
    public void testCacheExcpetion() {

        CacheException e = new CacheException(KEY);
        validateException(e, ONE, TEST, null);

        e = new CacheException(KEY, exception);
        validateException(e, ONE, TEST, exception);
    }

    @Test
    public void testConcurrencyExcpetion() {

        ConcurrencyException e = new ConcurrencyException(KEY);
        validateException(e, ONE, TEST, null);

        e = new ConcurrencyException(KEY, exception);
        validateException(e, ONE, TEST, exception);
    }

    @Test
    public void testConflictExcpetion() {

        ConflictException e = new ConflictException(KEY);
        validateException(e, ONE, TEST, null);

        e = new ConflictException(KEY, exception);
        validateException(e, ONE, TEST, exception);
    }

    @Test
    public void testConnectionExcpetion() {

        ConnectionException e = new ConnectionException(KEY);
        validateException(e, ONE, TEST, null);

        e = new ConnectionException(KEY, exception);
        validateException(e, ONE, TEST, exception);
    }

    @Test
    public void testInternalExcpetion() {

        InternalException e = new InternalException(KEY);
        validateException(e, ONE, TEST, null);

        e = new InternalException(KEY, exception);
        validateException(e, ONE, TEST, exception);
    }

    @Test
    public void testNotFoundExcpetion() {

        NotFoundException e = new NotFoundException(KEY);
        validateException(e, ONE, TEST, null);

        e = new NotFoundException(KEY, exception);
        validateException(e, ONE, TEST, exception);
    }

    @Test
    public void testSecurityExcpetion() {

        SecurityException e = new SecurityException(KEY);
        validateException(e, ONE, TEST, null);

        e = new SecurityException(KEY, exception);
        validateException(e, ONE, TEST, exception);
    }

    @Test
    public void testTokenExcpetion() {

        TokenException e = new TokenException(KEY);
        validateException(e, ONE, TEST, null);

        e = new TokenException(KEY, exception);
        validateException(e, ONE, TEST, exception);
    }

    @Test
    public void testValidationExcpetion() {

        ValidationException e = new ValidationException(KEY, TEST);
        validateException(e, ONE, TEST, null);
        assertEquals(TEST, e.getProperty());

        e = new ValidationException(KEY, TEST, exception);
        validateException(e, ONE, TEST, exception);
        assertEquals(TEST, e.getProperty());

        e = new ValidationException(KEY, TEST, exception);
        e.setProperty(CAUSE);
        validateException(e, ONE, TEST, exception);
        assertEquals(CAUSE, e.getProperty());
    }

    private void validateException(ServiceException e, Integer error,
            String message, Exception cause) {

        assertEquals(error, e.getError());
        assertEquals(message, e.getMessage());
        assertEquals(cause, e.getCause());
    }
}
