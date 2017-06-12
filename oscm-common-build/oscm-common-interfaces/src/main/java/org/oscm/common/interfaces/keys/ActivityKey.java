/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 6, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.keys;

import org.oscm.common.interfaces.data.Event;

/**
 * @author miethaner
 *
 */
public interface ActivityKey {

    public enum Type {
        COMMAND, QUERY, UPDATE, NONE
    }

    public String getKeyName();

    public Class<? extends Event> getInputClass();

    public Class<? extends Event> getOutputClass();

    public Type getType();

    public VersionKey getSince();

    public VersionKey getUntil();
}
