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

import java.util.UUID;

/**
 * Data class for entities.
 */
public class Identity {

    public static final String FIELD_ID = "id";
    public static final String FIELD_TIMESTAMP = "timestamp";
    public static final String FIELD_LINKS = "links";

    private UUID id;

    private Long timestamp;

    /**
     * Creates a new identity.
     *
     * @param id        the entities id, null returns null
     * @param timestamp the entities creation timestamp, null returns null
     */
    @JsonCreator
    public Identity(@JsonProperty(FIELD_ID) UUID id,
        @JsonProperty(FIELD_TIMESTAMP) Long timestamp) {
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
     * Gets the entities id.
     *
     * @return the id, null if not set
     */
    @JsonProperty(FIELD_ID)
    public UUID getId() {
        return id;
    }

    /**
     * Gets the entities id as string.
     *
     * @return the id string, null if not set
     */
    public String getIdString() {
        if (id != null) {
            return id.toString();
        } else {
            return "";
        }
    }

    /**
     * Gets the creation timestamp for this entity.
     *
     * @return the timestamp, null if not set
     */
    @JsonProperty(FIELD_TIMESTAMP)
    public Long getTimestamp() {
        return timestamp;
    }
}
