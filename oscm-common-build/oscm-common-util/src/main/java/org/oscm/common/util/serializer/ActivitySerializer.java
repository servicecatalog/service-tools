/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 12, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.util.serializer;

import java.lang.reflect.Type;

import org.oscm.common.interfaces.keys.ActivityKey;
import org.oscm.common.util.ConfigurationManager;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * @author miethaner
 *
 */
public class ActivitySerializer
        implements JsonSerializer<ActivityKey>, JsonDeserializer<ActivityKey> {

    @Override
    public ActivityKey deserialize(JsonElement json, Type typeOfT,
            JsonDeserializationContext context) throws JsonParseException {

        if (json == null || json.isJsonNull()) {
            return null;
        }

        return ConfigurationManager.getInstance()
                .getActivityForName(json.getAsString());
    }

    @Override
    public JsonElement serialize(ActivityKey src, Type typeOfSrc,
            JsonSerializationContext context) {

        if (src != null) {
            return context.serialize(src.getActivityName());
        } else {
            return null;
        }
    }

}
