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
import org.oscm.common.interfaces.exceptions.CacheException;
import org.oscm.common.interfaces.exceptions.ComponentException;
import org.oscm.common.interfaces.exceptions.ConcurrencyException;
import org.oscm.common.interfaces.exceptions.ConflictException;
import org.oscm.common.interfaces.exceptions.ConnectionException;
import org.oscm.common.interfaces.exceptions.InternalException;
import org.oscm.common.interfaces.exceptions.NotFoundException;
import org.oscm.common.interfaces.exceptions.SecurityException;
import org.oscm.common.interfaces.exceptions.TokenException;
import org.oscm.common.interfaces.exceptions.ValidationException;

/**
 * Unit tests for the common exceptions
 * 
 * @author miethaner
 */
public class ExceptionTest {

    private static final String TEST = "test";
    private static final String CAUSE = "cause";
    private static final Integer ONE = new Integer(1);

    private Exception exception = new Exception(CAUSE);

    @Test
    public void testCommponentExcpetion() {

        ComponentException e = new ComponentException(ONE, TEST);
        validateException(e, ONE, TEST, null);

        e = new ComponentException(ONE, exception);
        validateException(e, ONE, CAUSE, null);

        e = new ComponentException(ONE, TEST, exception);
        validateException(e, ONE, TEST, CAUSE);

        e = new ComponentException(ONE, TEST, TEST);
        validateException(e, ONE, TEST, TEST);

        e = new ComponentException(null, TEST);
        e.setError(ONE);
        e.setMoreInfo(TEST);
        validateException(e, ONE, TEST, TEST);
    }

    @Test
    public void testCacheExcpetion() {

        CacheException e = new CacheException(ONE, TEST);
        validateException(e, ONE, TEST, null);

        e = new CacheException(ONE, exception);
        validateException(e, ONE, CAUSE, null);

        e = new CacheException(ONE, TEST, exception);
        validateException(e, ONE, TEST, CAUSE);

        e = new CacheException(ONE, TEST, TEST);
        validateException(e, ONE, TEST, TEST);
    }

    @Test
    public void testConcurrencyExcpetion() {

        ConcurrencyException e = new ConcurrencyException(ONE, TEST);
        validateException(e, ONE, TEST, null);

        e = new ConcurrencyException(ONE, exception);
        validateException(e, ONE, CAUSE, null);

        e = new ConcurrencyException(ONE, TEST, exception);
        validateException(e, ONE, TEST, CAUSE);

        e = new ConcurrencyException(ONE, TEST, TEST);
        validateException(e, ONE, TEST, TEST);
    }

    @Test
    public void testConflictExcpetion() {

        ConflictException e = new ConflictException(ONE, TEST);
        validateException(e, ONE, TEST, null);

        e = new ConflictException(ONE, exception);
        validateException(e, ONE, CAUSE, null);

        e = new ConflictException(ONE, TEST, exception);
        validateException(e, ONE, TEST, CAUSE);

        e = new ConflictException(ONE, TEST, TEST);
        validateException(e, ONE, TEST, TEST);
    }

    @Test
    public void testConnectionExcpetion() {

        ConnectionException e = new ConnectionException(ONE, TEST);
        validateException(e, ONE, TEST, null);

        e = new ConnectionException(ONE, exception);
        validateException(e, ONE, CAUSE, null);

        e = new ConnectionException(ONE, TEST, exception);
        validateException(e, ONE, TEST, CAUSE);

        e = new ConnectionException(ONE, TEST, TEST);
        validateException(e, ONE, TEST, TEST);
    }

    @Test
    public void testInternalExcpetion() {

        InternalException e = new InternalException(ONE, TEST);
        validateException(e, ONE, TEST, null);

        e = new InternalException(ONE, exception);
        validateException(e, ONE, CAUSE, null);

        e = new InternalException(ONE, TEST, exception);
        validateException(e, ONE, TEST, CAUSE);

        e = new InternalException(ONE, TEST, TEST);
        validateException(e, ONE, TEST, TEST);
    }

    @Test
    public void testNotFoundExcpetion() {

        NotFoundException e = new NotFoundException(ONE, TEST);
        validateException(e, ONE, TEST, null);

        e = new NotFoundException(ONE, exception);
        validateException(e, ONE, CAUSE, null);

        e = new NotFoundException(ONE, TEST, exception);
        validateException(e, ONE, TEST, CAUSE);

        e = new NotFoundException(ONE, TEST, TEST);
        validateException(e, ONE, TEST, TEST);
    }

    @Test
    public void testSecurityExcpetion() {

        SecurityException e = new SecurityException(ONE, TEST);
        validateException(e, ONE, TEST, null);

        e = new SecurityException(ONE, exception);
        validateException(e, ONE, CAUSE, null);

        e = new SecurityException(ONE, TEST, exception);
        validateException(e, ONE, TEST, CAUSE);

        e = new SecurityException(ONE, TEST, TEST);
        validateException(e, ONE, TEST, TEST);
    }

    @Test
    public void testTokenExcpetion() {

        TokenException e = new TokenException(ONE, TEST);
        validateException(e, ONE, TEST, null);

        e = new TokenException(ONE, exception);
        validateException(e, ONE, CAUSE, null);

        e = new TokenException(ONE, TEST, exception);
        validateException(e, ONE, TEST, CAUSE);

        e = new TokenException(ONE, TEST, TEST);
        validateException(e, ONE, TEST, TEST);
    }

    @Test
    public void testValidationExcpetion() {

        ValidationException e = new ValidationException(ONE, TEST);
        validateException(e, ONE, TEST, null);

        e = new ValidationException(ONE, exception);
        validateException(e, ONE, CAUSE, null);

        e = new ValidationException(ONE, TEST, exception);
        e.setProperty(TEST);
        validateException(e, ONE, TEST, CAUSE);
        assertEquals(TEST, e.getProperty());

        e = new ValidationException(ONE, TEST, TEST);
        validateException(e, ONE, TEST, null);
        assertEquals(TEST, e.getProperty());

        e = new ValidationException(ONE, TEST, TEST, exception);
        validateException(e, ONE, TEST, CAUSE);
        assertEquals(TEST, e.getProperty());

        e = new ValidationException(ONE, TEST, TEST, TEST);
        validateException(e, ONE, TEST, TEST);
    }

    private void validateException(ComponentException e, Integer error,
            String message, String moreInfo) {

        assertEquals(error, e.getError());
        assertEquals(message, e.getMessage());
        assertEquals(moreInfo, e.getMoreInfo());
    }
}
