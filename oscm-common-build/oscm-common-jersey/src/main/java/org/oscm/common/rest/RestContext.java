/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 19, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest;

import org.oscm.common.interfaces.data.Token;
import org.oscm.common.interfaces.keys.ActivityKey;
import org.oscm.common.interfaces.keys.VersionKey;

/**
 * @author miethaner
 *
 */
public class RestContext {

    private VersionKey version;
    private ActivityKey activity;
    private Token token;

    public VersionKey getVersion() {
        return version;
    }

    public void setVersion(VersionKey version) {
        this.version = version;
    }

    public ActivityKey getActivity() {
        return activity;
    }

    public void setActivity(ActivityKey activity) {
        this.activity = activity;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
