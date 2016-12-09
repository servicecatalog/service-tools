/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 19, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.util.unit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.oscm.common.interfaces.exceptions.ValidationException;
import org.oscm.common.util.Validator;

/**
 * Unit test for Validator
 * 
 * @author miethaner
 */
public class ValidatorTest {

    @Test
    public void testValidateURL() throws Exception {

        String prop = "prop";

        Validator.validateURL(prop, null);
        Validator.validateURL(prop, "http://www.abc.de");

        try {
            Validator.validateURL(prop, "<>asdf");
        } catch (ValidationException e) {
            assertEquals(prop, e.getProperty());
        }
    }

    @Test
    public void testValidateText() throws Exception {

        String prop = "prop";

        Validator.validateText(prop, null);
        Validator.validateText(prop, "abcdefghijk_&%ÜEä#.");

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 30; i++) {
            sb.append("0123456789");
        }

        try {
            Validator.validateText(prop, sb.toString());
        } catch (ValidationException e) {
            assertEquals(prop, e.getProperty());
        }
    }

    @Test
    public void testValidateLang() throws Exception {

        String prop = "prop";

        Validator.validateLanguageCode(prop, null);
        Validator.validateLanguageCode(prop, "de");

        try {
            Validator.validateLanguageCode(prop, "ABCDE");
        } catch (ValidationException e) {
            assertEquals(prop, e.getProperty());
        }
    }

    @Test
    public void testValidateInteger() throws Exception {

        String prop = "prop";

        Validator.validateInteger(prop, null);
        Validator.validateInteger(prop, "1");

        try {
            Validator.validateInteger(prop, "abc");
        } catch (ValidationException e) {
            assertEquals(prop, e.getProperty());
        }
    }

    @Test
    public void testValidateLong() throws Exception {

        String prop = "prop";

        Validator.validateLong(prop, null);
        Validator.validateLong(prop, "1");

        try {
            Validator.validateLong(prop, "abc");
        } catch (ValidationException e) {
            assertEquals(prop, e.getProperty());
        }
    }

    @Test
    public void testValidateNotNull() throws Exception {

        String prop = "prop";

        Validator.validateNotNull(prop, "asdf");

        try {
            Validator.validateNotNull(prop, null);
        } catch (ValidationException e) {
            assertEquals(prop, e.getProperty());
        }
    }

    @Test
    public void testValidateEquals() throws Exception {

        String prop = "prop";

        Validator.validateEquals(prop, new Long(1), new Long(1));

        Validator.validateEquals(prop, null, null);

        try {
            Validator.validateEquals(prop, new Long(1), new Long(2));
        } catch (ValidationException e) {
            assertEquals(prop, e.getProperty());
        }

        try {
            Validator.validateEquals(prop, new Long(1), null);
        } catch (ValidationException e) {
            assertEquals(prop, e.getProperty());
        }
    }
}
