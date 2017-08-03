/*******************************************************************************
 *
 *  Copyright FUJITSU LIMITED 2017                                           
 *
 *  Creation Date: Jun 6, 2017                                                      
 *
 *******************************************************************************/

package org.oscm.lagom.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.annotation.concurrent.Immutable;
import java.util.Date;
import java.util.UUID;

/**
 * Entity class for events.
 *
 * @author miethaner
 */
@Immutable
@JsonSerialize
@JsonDeserialize
public class Identity {

    public static final String FIELD_ID = "id";
    public static final String FIELD_TIMESTAMP = "timestamp";
    public static final String FIELD_LINKS = "links";

    private UUID id;

    private Date timestamp;

    @JsonCreator
    public Identity(@JsonProperty(FIELD_ID) UUID id,
        @JsonProperty(FIELD_TIMESTAMP) Date timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }

    public Identity(Identity identity) {
        if (identity == null) {
            return;
        }

        this.id = identity.getId();
        this.timestamp = identity.getTimestamp();
    }

    /**
     * Gets the event id. Returns null if not set.
     *
     * @return the id or null
     */
    @JsonProperty(FIELD_ID)
    public UUID getId() {
        return id;
    }

    /**
     * Gets the event id as string. Returns an empty string if not set.
     *
     * @return the id string or empty string
     */
    public String getIdString() {
        if (id != null) {
            return id.toString();
        } else {
            return "";
        }
    }

    /**
     * Gets the timestamp for this event. Returns null if not set.
     *
     * @return the timestamp or null
     */
    @JsonProperty(FIELD_TIMESTAMP)
    public Date getTimestamp() {
        return timestamp;
    }
}
