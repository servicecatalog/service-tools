/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 12, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.util.serializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.oscm.common.interfaces.data.Event;
import org.oscm.common.interfaces.data.Result;
import org.oscm.common.interfaces.keys.ActivityKey;
import org.oscm.common.interfaces.keys.VersionKey;
import org.oscm.common.util.ConfigurationManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

/**
 * @author miethaner
 *
 */
public class ResultSerializer implements JsonDeserializer<Result> {

    private Gson gson;

    public ResultSerializer() {
        gson = new GsonBuilder().setDateFormat(ConfigurationManager.FORMAT_DATE)
                .registerTypeHierarchyAdapter(ActivityKey.class,
                        new ActivitySerializer())
                .registerTypeHierarchyAdapter(VersionKey.class,
                        new VersionSerializer())
                .registerTypeAdapter(Event.class, new EventSerializer(null))
                .create();
    }

    @Override
    public Result deserialize(JsonElement json, Type typeOfT,
            JsonDeserializationContext context) throws JsonParseException {

        Result result = gson.fromJson(json, Result.class);

        if (result.getCommand() == null) {
            return null;
        }

        JsonElement eventsJson = json.getAsJsonObject()
                .get(Result.FIELD_EVENTS);

        if (eventsJson == null) {
            return null;
        }

        List<Event> eventList = new ArrayList<>();
        JsonArray arrayJson = eventsJson.getAsJsonArray();
        Class<? extends Event> clazz = result.getCommand().getOutputClass();

        arrayJson.forEach((e) -> eventList.add(gson.fromJson(e, clazz)));

        result.setEvents(eventList);

        return result;
    }
}
