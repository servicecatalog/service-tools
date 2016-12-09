/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jun 22, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.oscm.common.interfaces.enums.Messages;
import org.oscm.common.interfaces.exceptions.ValidationException;

/**
 * Common class for validating fields.
 * 
 * @author miethaner
 */
public class Validator {

    public static final String PATTERN_TEXT = ".{0,250}";
    public static final String PATTERN_URL = "(?i)\\b(?:(?:https?)://)(?:\\S+(?::\\S*)?@)?(?:(?!(?:10|127)(?:\\.\\d{1,3}){3})(?!(?:169\\.254|192\\.168)(?:\\.\\d{1,3}){2})(?!172\\.(?:1[6-9]|2\\d|3[0-1])(?:\\.\\d{1,3}){2})(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[1-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|(?:(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)(?:\\.(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)*(?:\\.(?:[a-z\\u00a1-\\uffff]{2,}))\\.?)(?::\\d{2,5})?(?:[/?#]\\S*)?\\b";

    public static final Set<String> isoLanguages = new HashSet<>(
            Arrays.asList(Locale.getISOLanguages()));

    /**
     * Validates the given URL if it matches the pattern
     * 
     * @param property
     *            the name of the property
     * @param url
     *            the URL to validate
     * @throws ValidationException
     */
    public static void validateURL(String property, String url)
            throws ValidationException {

        if (url != null) {
            if (!url.matches(PATTERN_URL)) {
                throw new ValidationException(Messages.INVALID_URL.error(),
                        property, Messages.INVALID_URL.message());
            }
        }
    }

    /**
     * Validates the given text if it matches the pattern
     * 
     * @param property
     *            the name of the property
     * @param text
     *            the text to validate
     * @throws ValidationException
     */
    public static void validateText(String property, String text)
            throws ValidationException {

        if (text != null) {
            if (!text.matches(PATTERN_TEXT)) {
                throw new ValidationException(Messages.INVALID_TEXT.error(),
                        property, Messages.INVALID_TEXT.message());
            }
        }
    }

    /**
     * Validates the given language code if it matches the pattern
     * 
     * @param property
     *            the name of the property
     * @param language
     *            the language code to validate
     * @throws ValidationException
     */
    public static void validateLanguageCode(String property, String language)
            throws ValidationException {

        if (language != null) {
            if (!isoLanguages.contains(language)) {
                throw new ValidationException(Messages.INVALID_LANGUAGE.error(),
                        property, Messages.INVALID_LANGUAGE.message());
            }
        }
    }

    /**
     * Validates the given integer if it matches the pattern and parses it.
     * 
     * @param property
     *            the name of the property
     * @param number
     *            the integer to validate and parse
     * @throws ValidationException
     */
    public static Integer validateInteger(String property, String number)
            throws ValidationException {

        if (number == null) {
            return null;
        }

        try {
            return Integer.valueOf(number);
        } catch (NumberFormatException e) {
            throw new ValidationException(Messages.INVALID_NUMBER.error(),
                    property, Messages.INVALID_NUMBER.message());
        }
    }

    /**
     * Validates the given long if it matches the pattern and parses it.
     * 
     * @param property
     *            the name of the property
     * @param number
     *            the integer to validate and parse
     * @throws ValidationException
     */
    public static Long validateLong(String property, String number)
            throws ValidationException {

        if (number == null) {
            return null;
        }

        try {
            return Long.valueOf(number);
        } catch (NumberFormatException e) {
            throw new ValidationException(Messages.INVALID_NUMBER.error(),
                    property, Messages.INVALID_NUMBER.message());
        }
    }

    /**
     * Validates the given object if it is not null.
     * 
     * @param property
     *            the name of the property
     * @param obj
     *            the object to validate
     * @throws ValidationException
     */
    public static void validateNotNull(String property, Object obj)
            throws ValidationException {

        if (obj == null) {
            throw new ValidationException(Messages.IS_NULL.error(), property,
                    Messages.IS_NULL.message());
        }
    }

    /**
     * Validates the given objects if they are equal.
     * 
     * @param property
     *            the name of the property
     * @param first
     *            the first object
     * @param second
     *            the second object
     * @throws ValidationException
     */
    public static void validateEquals(String property, Object first,
            Object second) throws ValidationException {

        if (first != null && second != null) {
            if (!first.equals(second)) {
                throw new ValidationException(Messages.NOT_EQUAL.error(),
                        property, Messages.NOT_EQUAL.message());
            }
        } else if (first != second) {
            throw new ValidationException(Messages.NOT_EQUAL.error(), property,
                    Messages.NOT_EQUAL.message());
        }
    }

    private Validator() {
    }
}
