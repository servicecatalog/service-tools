/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jan 12, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.kafka;

import java.util.Map;

import com.google.gson.annotations.SerializedName;

/**
 * Representation class to wrap data for requests.
 * 
 * @author miethaner
 */
public class Request<R extends Representation> extends Representation {

    public static final String FIELD_PARAMETERS = "parameters";
    public static final String FIELD_ITEM = "item";

    @SerializedName(FIELD_PARAMETERS)
    private Map<String, String> parameters;

    @SerializedName(FIELD_ITEM)
    private R item;

    public Request() {
    }

    public Request(R item, Map<String, String> parameters) {
        this.item = item;
        this.parameters = parameters;
    }

    public R getItem() {
        return item;
    }

    public void setItem(R item) {
        this.item = item;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    @Override
    public void update() {
        super.update();

        if (item != null) {
            item.update();
        }
    }

}
