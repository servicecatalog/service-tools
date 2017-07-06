/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 12, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.util.serializer;

import java.lang.reflect.Type;

import org.oscm.common.interfaces.data.Version;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * De-/Serializer for {@link VersionKey} types for gson.
 * 
 * @author miethaner
 */
public class VersionSerializer
        implements JsonSerializer<Version>, JsonDeserializer<Version> {

    @Override
    public Version deserialize(JsonElement json, Type typeOfT,
            JsonDeserializationContext context) throws JsonParseException {

        if (json == null || json.isJsonNull()) {
            return null;
        }

        return new Version(json.getAsInt());
    }

    @Override
    public JsonElement serialize(Version src, Type typeOfSrc,
            JsonSerializationContext context) {

        if (src != null) {
            return context.serialize(new Integer(src.getCompiledVersion()));
        } else {
            return null;
        }
    }

}
