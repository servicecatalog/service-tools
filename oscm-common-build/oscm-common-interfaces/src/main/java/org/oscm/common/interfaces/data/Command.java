/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 6, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.data;

import java.util.Date;
import java.util.UUID;

import org.oscm.common.interfaces.keys.ActivityKey;
import org.oscm.common.interfaces.keys.VersionKey;

import com.google.gson.annotations.SerializedName;

/**
 * @author miethaner
 *
 */
public class Command extends VersionedEntity {

    public static final String FIELD_ID = "id";
    public static final String FIELD_CMD = "command";
    public static final String FIELD_EVENT = "event";
    public static final String FIELD_TOKEN = "token";
    public static final String FIELD_TIMESTAMP = "timestamp";

    @SerializedName(FIELD_ID)
    private UUID id;

    @SerializedName(FIELD_CMD)
    private ActivityKey command;

    @SerializedName(FIELD_EVENT)
    private Event event;

    @SerializedName(FIELD_TOKEN)
    private Token token;

    @SerializedName(FIELD_TIMESTAMP)
    private Date timestamp;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ActivityKey getCommand() {
        return command;
    }

    public void setCommand(ActivityKey command) {
        this.command = command;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public void setVersion(VersionKey version) {
        super.setVersion(version);

        if (event != null) {
            event.setVersion(version);
        }

        if (token != null) {
            token.setVersion(version);
        }
    }
}
