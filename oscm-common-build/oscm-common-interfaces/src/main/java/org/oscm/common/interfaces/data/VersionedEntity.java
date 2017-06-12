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
 * 
 * @author miethaner
 */
@SuppressWarnings("unused")
public abstract class VersionedEntity {

    public static final String FIELD_VERSION = "version";

    @SerializedName(FIELD_VERSION)
    private VersionKey version;

    public VersionKey getVersion() {
        return version;
    }

    public void setVersion(VersionKey version) {
        this.version = version;
    }

    public void updateFrom(VersionKey version) {
        // nothing to update
    }

    public void convertTo(VersionKey version) {
        // nothing to convert
    }
}
