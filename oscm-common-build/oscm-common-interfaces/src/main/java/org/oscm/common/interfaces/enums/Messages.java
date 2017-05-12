/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jul 13, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.enums;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.oscm.common.interfaces.config.MessageKey;

/**
 * Enum for exceptions to provide error code and message.
 * 
 * @author miethaner
 */
public enum Messages implements MessageKey {

    DEBUG(0), //
    ERROR(1), //

    JSON_FORMAT(2), //
    INVALID_ID(3), //
    INVALID_VERSION(4), //
    INVALID_ETAG(5), //
    INVALID_NUMBER(6), //
    INVALID_URL(7), //
    INVALID_TEXT(8), //
    INVALID_LANGUAGE(9), //
    IS_NULL(10), //
    NOT_EQUAL(11), //
    METHOD_VERSION(12), //
    BAD_PROPERTY(13), //
    NO_CONTENT(14), //
    MANDATORY_PROPERTY_NOT_PRESENT(15), //

    NOT_SECURE(16), //
    NOT_AUTHENTICATED(17), //
    NOT_AUTHORIZED(18), //

    LOGIN_FAILD(19), //

    UNSUPPORTED_METHOD(20); //

    private static final String BUNDLE = "org.oscm.common.interferces.messages.Messages";

    private final int code;

    private Messages(int error) {
        this.code = error;
    }

    @Override
    public String getKeyName() {
        return name();
    }

    @Override
    public Integer getCode() {
        return new Integer(code);
    }

    @Override
    public String getMessage(String... values) {
        String msg;
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE);
            msg = bundle.getString(Integer.toString(code));
        } catch (MissingResourceException e) {
            throw new RuntimeException(
                    "Unable to find message resource bundle");
        }

        return MessageFormat.format(msg, (Object[]) values);
    }
}
