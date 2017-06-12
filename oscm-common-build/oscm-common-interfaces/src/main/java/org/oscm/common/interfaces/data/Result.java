/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 6, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.oscm.common.interfaces.enums.Status;
import org.oscm.common.interfaces.keys.ActivityKey;
import org.oscm.common.interfaces.keys.VersionKey;

import com.google.gson.annotations.SerializedName;

/**
 * @author miethaner
 *
 */
public class Result extends VersionedEntity {

    public static final String FIELD_ID = "id";
    public static final String FIELD_PARENT = "parent";
    public static final String FIELD_CMD = "command";
    public static final String FIELD_STATE = "state";
    public static final String FIELD_EVENTS = "events";
    public static final String FIELD_FAILURE = "failure";
    public static final String FIELD_TIMESTAMP = "timestamp";

    @SerializedName(FIELD_ID)
    private UUID id;

    @SerializedName(FIELD_PARENT)
    private UUID parent;

    @SerializedName(FIELD_CMD)
    private ActivityKey command;

    @SerializedName(FIELD_STATE)
    private Status status;

    @SerializedName(FIELD_EVENTS)
    private List<Event> events;

    @SerializedName(FIELD_FAILURE)
    private Failure failure;

    @SerializedName(FIELD_TIMESTAMP)
    private Date timestamp;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getParent() {
        return parent;
    }

    public void setParent(UUID parent) {
        this.parent = parent;
    }

    public ActivityKey getCommand() {
        return command;
    }

    public void setCommand(ActivityKey command) {
        this.command = command;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public Failure getFailure() {
        return failure;
    }

    public void setFailure(Failure failure) {
        this.failure = failure;
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

        if (events != null) {
            for (Event e : events) {
                e.setVersion(version);
            }
        }

        if (failure != null) {
            failure.setVersion(version);
        }
    }
}
