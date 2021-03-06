/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 6, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.data;

import java.util.List;
import java.util.UUID;

import org.oscm.common.interfaces.enums.Status;
import org.oscm.common.interfaces.keys.ActivityKey;

import com.google.gson.annotations.SerializedName;

/**
 * Entity class for results.
 * 
 * @author miethaner
 */
public class Result extends VersionedEntity {

    public static final String FIELD_ID = "id";
    public static final String FIELD_CMD = "command";
    public static final String FIELD_STATE = "state";
    public static final String FIELD_EVENTS = "events";
    public static final String FIELD_FAILURE = "failure";

    @SerializedName(FIELD_ID)
    private UUID id;

    @SerializedName(FIELD_CMD)
    private ActivityKey command;

    @SerializedName(FIELD_STATE)
    private Status status;

    @SerializedName(FIELD_EVENTS)
    private List<Event> events;

    @SerializedName(FIELD_FAILURE)
    private Failure failure;

    /**
     * Gets the command id of the parent. Returns null if not set.
     * 
     * @return the id or null
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the command id of the parent.
     * 
     * @param id
     *            the result id
     */
    public void setId(UUID id) {
        this.id = id;
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
     * Sets the version with the given version key for this entity and all its
     * sub entities.
     * 
     * @param version
     *            the version key
     */
    @Override
    public void setVersion(Version version) {
        super.setVersion(version);

        if (events != null) {
            events.forEach((e) -> e.setVersion(version));
        }

        if (failure != null) {
            failure.setVersion(version);
        }
    }
}
