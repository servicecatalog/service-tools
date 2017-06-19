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
 * @author miethaner
 *
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

    public class Constants {
        public static final String OPTION_CACHE = "CACHE";
        public static final String OPTION_CONCURRENCY = "CONCURRENCY";
        public static final String OPTION_CONFLICT = "CONFLICT";
        public static final String OPTION_CONNECTION = "CONNECTION";
        public static final String OPTION_INTERNAL = "INTERNAL";
        public static final String OPTION_NOT_FOUND = "NOT_FOUND";
        public static final String OPTION_SECURITY = "SECURITY";
        public static final String OPTION_TIMEOUT = "TIMEOUT";
        public static final String OPTION_TOKEN = "TOKEN";
        public static final String OPTION_VALIDATION = "VALIDATION";
        public static final String OPTION_OTHER = "OTHER";
    }
}
