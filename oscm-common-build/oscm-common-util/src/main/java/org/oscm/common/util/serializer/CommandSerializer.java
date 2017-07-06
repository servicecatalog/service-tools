/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 12, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.util.serializer;

import java.lang.reflect.Type;

import org.oscm.common.interfaces.data.Command;
import org.oscm.common.interfaces.data.Event;
import org.oscm.common.interfaces.data.Version;
import org.oscm.common.interfaces.keys.ActivityKey;
import org.oscm.common.util.ConfigurationManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

/**
 * De-/Serializer for {@link Command} types for gson.
 * 
 * @author miethaner
 */
public class CommandSerializer implements JsonDeserializer<Command> {

    private Gson gson;

    public CommandSerializer() {
        gson = new GsonBuilder().setDateFormat(ConfigurationManager.FORMAT_DATE)
                .registerTypeHierarchyAdapter(ActivityKey.class,
                        new ActivitySerializer())
                .registerTypeHierarchyAdapter(Version.class,
                        new VersionSerializer())
                .registerTypeAdapter(Event.class, new EventSerializer(null))
                .create();
    }

    @Override
    public Command deserialize(JsonElement json, Type typeOfT,
            JsonDeserializationContext context) throws JsonParseException {

        Command command = gson.fromJson(json, Command.class);

        if (command.getCommand() == null) {
            return null;
        }

        JsonElement eventJson = json.getAsJsonObject().get(Command.FIELD_EVENT);

        Class<? extends Event> clazz = command.getCommand().getInputEntity()
                .getEntityClass();
        Event event = gson.fromJson(eventJson, clazz);
        command.setEvent(event);

        return command;
    }
}
