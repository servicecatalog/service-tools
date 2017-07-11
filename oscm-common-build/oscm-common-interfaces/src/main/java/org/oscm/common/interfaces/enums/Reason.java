/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 8, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.enums;

import com.google.gson.annotations.SerializedName;

/**
 * Enum for failure reasons.
 * 
 * @author miethaner
 */
public enum Reason {
    @SerializedName(Constants.OPTION_CACHE)
    CACHE, //

    @SerializedName(Constants.OPTION_CONCURRENCY)
    CONCURRENCY, //

    @SerializedName(Constants.OPTION_CONFLICT)
    CONFLICT, //

    @SerializedName(Constants.OPTION_CONNECTION)
    CONNECTION, //

    @SerializedName(Constants.OPTION_INTERNAL)
    INTERNAL, //

    @SerializedName(Constants.OPTION_NOT_FOUND)
    NOT_FOUND, //

    @SerializedName(Constants.OPTION_SECURITY)
    SECURITY, //

    @SerializedName(Constants.OPTION_TIMEOUT)
    TIMEOUT, //

    @SerializedName(Constants.OPTION_TOKEN)
    TOKEN, //

    @SerializedName(Constants.OPTION_VALIDATION)
    VALIDATION, //

    @SerializedName(Constants.OPTION_OTHER)
    OTHER;

    public static class Constants {
        public static final String OPTION_CACHE = "cache";
        public static final String OPTION_CONCURRENCY = "concurrency";
        public static final String OPTION_CONFLICT = "conflict";
        public static final String OPTION_CONNECTION = "connection";
        public static final String OPTION_INTERNAL = "internal";
        public static final String OPTION_NOT_FOUND = "not_found";
        public static final String OPTION_SECURITY = "security";
        public static final String OPTION_TIMEOUT = "timeout";
        public static final String OPTION_TOKEN = "token";
        public static final String OPTION_VALIDATION = "validation";
        public static final String OPTION_OTHER = "other";
    }
}
