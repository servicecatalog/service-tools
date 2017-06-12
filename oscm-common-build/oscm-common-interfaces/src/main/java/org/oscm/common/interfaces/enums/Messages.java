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

import org.oscm.common.interfaces.keys.MessageKey;

/**
 * Enum for exceptions to provide error code and message.
 * 
 * @author miethaner
 */
public enum Messages implements MessageKey {

    DEBUG(0), //
    INFO(0), //
    ERROR(1), //

    JSON_FORMAT(10), //
    INVALID_ID(11), //
    INVALID_VERSION(12), //
    INVALID_UUID(13), //
    INVALID_NUMBER(14), //
    INVALID_URL(15), //
    INVALID_TEXT(16), //
    INVALID_LANGUAGE(17), //
    PROPERTY_IS_NULL(18), //
    PROPERTY_NOT_EQUAL(19), //
    BAD_PROPERTY(20), //
    METHOD_VERSION(21), //
    NO_CONTENT(22), //
    MANDATORY_PROPERTY_NOT_PRESENT(23), //

    NOT_SECURE(24), //
    NOT_AUTHENTICATED(25), //
    NOT_AUTHORIZED(26), //

    LOGIN_FAILED(27), //

    UNSUPPORTED_METHOD(28), //

    ENTITY_ALREADY_EXISTS(29), //
    ENTITY_NOT_FOUND(30), //
    ENTITY_NOT_MODIFIED(31), //
    ENTITY_WAS_MODIFIED(32), //
    ENTITY_NOT_UNIQUE(33), //
    NO_RESULT(34), //

    CONNECTION_FAILURE(35), //
    BAD_RESPONSE(36); //

    private static final String BUNDLE = "org.oscm.common.interfaces.messages.Messages";

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
