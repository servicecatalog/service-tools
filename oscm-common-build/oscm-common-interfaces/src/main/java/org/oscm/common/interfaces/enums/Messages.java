/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jul 13, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.enums;

/**
 * Enum for exceptions to provide error code and message.
 * 
 * @author miethaner
 */
public enum Messages {

    DEBUG(0, "Debug"), //
    ERROR(1, "Error"), //

    JSON_FORMAT(2, "Invalid JSON format"), //
    INVALID_ID(3, "ID not valid or unknown"), //
    INVALID_VERSION(4, "Version not valid or unknown"), //
    INVALID_ETAG(5, "Invalid ETag"), //
    INVALID_NUMBER(6, "Invalid number"), //
    INVALID_URL(7, "Invalid URL"), //
    INVALID_TEXT(8, "Invalid text"), //
    INVALID_LANGUAGE(9, "Invalid language ISO code"), //
    IS_NULL(10, "Property is null"), //
    NOT_EQUAL(11, "Properties are not equal"), //
    METHOD_VERSION(12, "Method not available for used version"), //
    BAD_PROPERTY(13, "Property does not match allowed pattern"), //
    NO_CONTENT(14, "No content in request while expected"), //
    MANDATORY_PROPERTY_NOT_PRESENT(15,
            "One or more mandatory properties are not present"), //

    NOT_SECURE(16, "Connection is not secure"), //
    NOT_AUTHENTICATED(17, "User is not authenticated"), //
    NOT_AUTHORIZED(18, "User is not authorized for the operation"), //

    LOGIN_FAILD(19, "Login failed"), //

    UNSUPPORTED_METHOD(20, "The method is not supported by this service"); //

    private final int error;
    private final String message;

    private Messages(int error, String message) {
        this.error = error;
        this.message = message;
    }

    /**
     * Gets the error code for this entry.
     * 
     * @return the error code
     */
    public Integer error() {
        return new Integer(error);
    }

    /**
     * Gets the message for this entry.
     * 
     * @return the message
     */
    public String message() {
        return message;
    }
}
