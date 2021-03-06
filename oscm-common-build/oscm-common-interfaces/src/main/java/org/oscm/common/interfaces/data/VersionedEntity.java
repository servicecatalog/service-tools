/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Dec 6, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.data;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

/**
 * Super class for all versionable entities.
 * 
 * @author miethaner
 */
public abstract class VersionedEntity {

    public static final String FIELD_VERSION = "version";
    public static final String FIELD_TIMESTAMP = "timestamp";

    @SerializedName(FIELD_VERSION)
    private Version version;

    @SerializedName(FIELD_TIMESTAMP)
    private Date timestamp;

    /**
     * Gets the version key for this entity. Returns null if not set.
     * 
     * @return the key or null
     */
    public Version getVersion() {
        return version;
    }

    /**
     * Sets the version with the given version key for this entity.
     * 
     * @param version
     *            the version key
     */
    public void setVersion(Version version) {
        this.version = version;
    }

    /**
     * Gets the timestamp for when this event was created. Returns null if not
     * set.
     * 
     * @return the timestamp or null
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp for when this event was created.
     * 
     * @param timestamp
     *            the timestamp
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * <strong>Overwrite this method if the entity needs to be
     * versioned.</strong> <br/>
     * Updates the entity from the version of the given version key to the
     * current one. This method is called by deserializers before the entity is
     * passed for processing.
     * 
     * @param version
     *            the version key to update from
     */
    public void updateFrom(Version version) {
        // nothing to update
    }

    /**
     * <strong>Overwrite this method if the entity needs to be
     * versioned.</strong> <br/>
     * Converts the entity from the current version to the version of the given
     * key. This method is called by serializers before the entity is passed to
     * external systems.
     * 
     * @param version
     *            the version key to convert to
     */
    public void convertTo(Version version) {
        // nothing to convert
    }
}
