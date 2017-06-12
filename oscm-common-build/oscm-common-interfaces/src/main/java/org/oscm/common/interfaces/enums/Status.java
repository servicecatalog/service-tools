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
public enum Status {
    @SerializedName(Constants.OPTION_SUCCESS)
    SUCCESS, //

    @SerializedName(Constants.OPTION_FAILURE)
    FAILURE, //

    @SerializedName(Constants.OPTION_PENDING)
    PENDING, //

    @SerializedName(Constants.OPTION_CACHED)
    CACHED;

    public class Constants {
        public static final String OPTION_SUCCESS = "success";
        public static final String OPTION_FAILURE = "failure";
        public static final String OPTION_PENDING = "pending";
        public static final String OPTION_CACHED = "cached";
    }
}
