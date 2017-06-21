/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Dec 6, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.data;

import org.oscm.common.interfaces.keys.VersionKey;

import com.google.gson.annotations.SerializedName;

/**
 * Super class for all versionable entities.
 * 
 * @author miethaner
 */
public abstract class VersionedEntity {

    public static final String FIELD_VERSION = "version";

    @SerializedName(FIELD_VERSION)
    private VersionKey version;

    /**
     * Gets the version key for this entity. Returns null if not set.
     * 
     * @return the key or null
     */
    public VersionKey getVersion() {
        return version;
    }

    /**
     * Sets the version with the given version key for this entity.
     * 
     * @param version
     *            the version key
     */
    public void setVersion(VersionKey version) {
        this.version = version;
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
    public void updateFrom(VersionKey version) {
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
    public void convertTo(VersionKey version) {
        // nothing to convert
    }
}
