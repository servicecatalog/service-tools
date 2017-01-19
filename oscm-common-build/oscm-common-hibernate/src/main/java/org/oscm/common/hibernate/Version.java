/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jan 18, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Versioning class for database instances
 * 
 * @author miethaner
 */
@Entity(name = Version.ENTITY_NAME)
public class Version {

    public static final String ENTITY_NAME = "Version";
    public static final String FIELD_VERSION = "version";

    @Column(name = FIELD_VERSION, nullable = false)
    private int version;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
