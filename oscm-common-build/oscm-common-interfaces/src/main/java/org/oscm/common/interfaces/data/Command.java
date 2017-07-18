/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 6, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.data;

import java.util.UUID;

import org.oscm.common.interfaces.keys.ActivityKey;

import com.google.gson.annotations.SerializedName;

/**
 * Entity class for commands.
 * 
 * @author miethaner
 */
public class Command extends VersionedEntity {

    public static final String FIELD_ID = "id";
    public static final String FIELD_CMD = "command";
    public static final String FIELD_EVENT = "event";
    public static final String FIELD_TOKEN = "token";

    @SerializedName(FIELD_ID)
    private UUID id;

    @SerializedName(FIELD_CMD)
    private ActivityKey command;

    @SerializedName(FIELD_EVENT)
    private Event event;

    @SerializedName(FIELD_TOKEN)
    private Token token;

    /**
     * Gets the command id. Returns null if not set.
     * 
     * @return the id or null
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the command id.
     * 
     * @param id
     *            the command id
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Gets the command key. Returns null if not set.
     * 
     * @return the key or null
     */
    public ActivityKey getCommand() {
        return command;
    }

    /**
     * Sets the command key.
     * 
     * @param command
     *            the command key
     */
    public void setCommand(ActivityKey command) {
        this.command = command;
    }

    /**
     * Gets the input event for this command. Returns null if not set.
     * 
     * @return the event or null
     */
    public Event getEvent() {
        return event;
    }

    /**
     * Sets the input event for this command.
     * 
     * @param event
     *            the input event
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     * Gets the security token of the requesting user. Returns null if not set.
     * 
     * @return the token or null
     */
    public Token getToken() {
        return token;
    }

    /**
     * Sets the security token of the requesting user.
     * 
     * @param token
     *            the security token
     */
    public void setToken(Token token) {
        this.token = token;
    }

    /**
     * Sets the version with the given version key for this entity and all its
     * sub entities.
     * 
     * @param version
     *            the version key
     */
    @Override
    public void setVersion(Version version) {
        super.setVersion(version);

        if (event != null) {
            event.setVersion(version);
        }

        if (token != null) {
            token.setVersion(version);
        }
    }
}
