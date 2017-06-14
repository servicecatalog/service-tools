/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jun 29, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.enums;

import com.google.gson.annotations.SerializedName;

/**
 * Enum for basic C(R)UD operations and more.
 * 
 * @author miethaner
 */
public enum Operation {

    @SerializedName(Constants.OPTION_UPDATE)
    UPDATE, //

    @SerializedName(Constants.OPTION_DELETE)
    DELETE; //

    public class Constants {
        public static final String OPTION_UPDATE = "UPD";
        public static final String OPTION_DELETE = "DEL";
    }
}
