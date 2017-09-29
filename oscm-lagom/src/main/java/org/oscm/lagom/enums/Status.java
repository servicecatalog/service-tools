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
 * Enum for result states.
 *
 * @author miethaner
 */
public enum Status {
    @JsonProperty(Constants.OPTION_SUCCESS)
    SUCCESS, //

    @JsonProperty(Constants.OPTION_FAILED)
    FAILED, //

    @JsonProperty(Constants.OPTION_PENDING)
    PENDING, //

    @JsonProperty(Constants.OPTION_CACHED)
    CACHED;

    public static class Constants {
        public static final String OPTION_SUCCESS = "success";
        public static final String OPTION_FAILED = "failed";
        public static final String OPTION_PENDING = "pending";
        public static final String OPTION_CACHED = "cached";
    }
}
