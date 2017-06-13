/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 12, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.util.serializer;

import java.lang.reflect.Type;

import org.oscm.common.interfaces.keys.VersionKey;
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
public class VersionSerializer
        implements JsonSerializer<VersionKey>, JsonDeserializer<VersionKey> {

    @Override
    public VersionKey deserialize(JsonElement json, Type typeOfT,
            JsonDeserializationContext context) throws JsonParseException {

        if (json == null || json.isJsonNull()) {
            return null;
        }

        return ConfigurationManager.getInstance()
                .getVersionForCompiled(json.getAsInt());
    }

    @Override
    public JsonElement serialize(VersionKey src, Type typeOfSrc,
            JsonSerializationContext context) {

        if (src != null) {
            return context.serialize(new Integer(src.getCompiledVersion()));
        } else {
            return null;
        }
    }

}
