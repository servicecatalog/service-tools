/*******************************************************************************
 *
 *  Copyright FUJITSU LIMITED 2017                                           
 *
 *  Creation Date: Jun 8, 2017                                                      
 *
 *******************************************************************************/

package org.oscm.lagom.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum for failure reasons.
 *
 * @author miethaner
 */
public enum Reason {
    @JsonProperty(Constants.OPTION_CACHE)
    CACHE, //

    @JsonProperty(Constants.OPTION_CONCURRENCY)
    CONCURRENCY, //

    @JsonProperty(Constants.OPTION_CONFLICT)
    CONFLICT, //

    @JsonProperty(Constants.OPTION_CONNECTION)
    CONNECTION, //

    @JsonProperty(Constants.OPTION_INTERNAL)
    INTERNAL, //

    @JsonProperty(Constants.OPTION_NOT_FOUND)
    NOT_FOUND, //

    @JsonProperty(Constants.OPTION_SECURITY)
    SECURITY, //

    @JsonProperty(Constants.OPTION_TIMEOUT)
    TIMEOUT, //

    @JsonProperty(Constants.OPTION_TOKEN)
    TOKEN, //

    @JsonProperty(Constants.OPTION_VALIDATION)
    VALIDATION, //

    @JsonProperty(Constants.OPTION_OTHER)
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
