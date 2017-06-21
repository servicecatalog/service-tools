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
 * Entity class for results.
 * 
 * @author miethaner
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

    /**
     * Gets the result id. Returns null if not set.
     * 
     * @return the id or null
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the result id.
     * 
     * @param id
     *            the result id
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Gets the parent id. Returns null if not set.
     * 
     * @return the id or null
     */
    public UUID getParent() {
        return parent;
    }

    /**
     * Sets the parent id.
     * 
     * @param parent
     *            the parent id
     */
    public void setParent(UUID parent) {
        this.parent = parent;
    }

    /**
     * Gets the parent command key. Returns null if not set.
     * 
     * @return the key or null
     */
    public ActivityKey getCommand() {
        return command;
    }

    /**
     * Sets the parent command key.
     * 
     * @param command
     *            the parent key
     */
    public void setCommand(ActivityKey command) {
        this.command = command;
    }

    /**
     * Gets the results status. Returns null if not set.
     * 
     * @return the status or null
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets the results status.
     * 
     * @param status
     *            the status
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Gets the list of resulting events. Returns null if not set.
     * 
     * @return list of events or null
     */
    public List<Event> getEvents() {
        return events;
    }

    /**
     * Sets the list of resulting events.
     * 
     * @param events
     *            the list of events
     */
    public void setEvents(List<Event> events) {
        this.events = events;
    }

    /**
     * Gets the results failure. Returns null if not set.
     * 
     * @return the failure or null
     */
    public Failure getFailure() {
        return failure;
    }

    /**
     * Sets the results failure.
     * 
     * @param failure
     *            the failure
     */
    public void setFailure(Failure failure) {
        this.failure = failure;
    }

    /**
     * Gets the creation timestamp for this result. Returns null if not set.
     * 
     * @return the timestamp or null
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the creation timestamp for this result.
     * 
     * @param timestamp
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Sets the version with the given version key for this entity and all its
     * sub entities.
     * 
     * @param version
     *            the version key
     */
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
