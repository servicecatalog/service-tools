/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 12, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.util.serializer;

import java.lang.reflect.Type;

import org.oscm.common.interfaces.data.Event;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

/**
 * De-/Serializer for {@link Event} types for gson.
 * 
 * @author miethaner
 */
public class EventSerializer implements JsonDeserializer<Event> {

    private Class<? extends Event> clazz;

    /**
     * Creates a new event deserializer for the given subclass.
     * 
     * @param clazz
     *            the event subclass
     */
    public EventSerializer(Class<? extends Event> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Event deserialize(JsonElement json, Type typeOfT,
            JsonDeserializationContext context) throws JsonParseException {

        if (clazz != null) {
            return context.deserialize(json, clazz);
        } else {
            return null;
        }
    }

}
