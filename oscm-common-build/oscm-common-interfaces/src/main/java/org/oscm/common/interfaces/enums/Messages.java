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
    INFO(1), //
    ERROR(2), //

    DEBUG_COMMAND(101), //
    DEBUG_QUERY(102), //

    INFO_SERVICE_START(201), //
    INFO_SERVICE_STOP(202), //
    INFO_SERVICE_READY(203), //

    ERROR_TIMEOUT(501), //

    ERROR_JSON_FORMAT(601), //
    ERROR_INVALID_ID(602), //
    ERROR_INVALID_ACTIVITY(603), //
    ERROR_INVALID_VERSION(604), //
    ERROR_INVALID_UUID(605), //
    ERROR_INVALID_NUMBER(606), //
    ERROR_INVALID_URL(607), //
    ERROR_INVALID_TEXT(608), //
    ERROR_INVALID_LANGUAGE(609), //
    ERROR_PROPERTY_IS_NULL(610), //
    ERROR_PROPERTY_NOT_EQUAL(611), //
    ERROR_BAD_PROPERTY(612), //
    ERROR_METHOD_VERSION(613), //
    ERROR_NO_CONTENT(614), //
    ERROR_MANDATORY_PROPERTY_NOT_PRESENT(615), //

    ERROR_NOT_SECURE(701), //
    ERROR_NOT_AUTHENTICATED(702), //
    ERROR_NOT_AUTHORIZED(703), //
    ERROR_LOGIN_FAILED(704), //

    ERROR_ALREADY_EXISTS(801), //
    ERROR_NOT_FOUND(802), //
    ERROR_NOT_MODIFIED(803), //
    ERROR_WAS_MODIFIED(804), //
    ERROR_NOT_UNIQUE(805), //
    ERROR_NO_RESULT(806), //

    ERROR_CONNECTION_FAILURE(901), //
    ERROR_BAD_RESPONSE(902); //

    private static final String BUNDLE = "org.oscm.common.interfaces.messages.Messages";

    private final int code;

    private Messages(int error) {
        this.code = error;
    }

    @Override
    public Integer getCode() {
        return Integer.valueOf(code);
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
