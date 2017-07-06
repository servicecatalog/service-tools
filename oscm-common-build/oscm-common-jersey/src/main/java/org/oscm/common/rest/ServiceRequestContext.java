/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 19, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest;

import org.oscm.common.interfaces.data.Token;
import org.oscm.common.interfaces.data.Version;
import org.oscm.common.interfaces.keys.ActivityKey;

/**
 * Context class for service requests.
 * 
 * @author miethaner
 */
public class ServiceRequestContext {

    private Version version;
    private ActivityKey activity;
    private Token token;

    /**
     * Gets the version key for his context. Returns null if not set.
     * 
     * @return the key or null
     */
    public Version getVersion() {
        return version;
    }

    /**
     * Sets the version with the given version key for this context.
     * 
     * @param version
     *            the version key
     */
    public void setVersion(Version version) {
        this.version = version;
    }

    /**
     * Gets the activity key for this context. Returns null if not set.
     * 
     * @return the key or null
     */
    public ActivityKey getActivity() {
        return activity;
    }

    /**
     * Sets the activity key for this context.
     * 
     * @param activity
     *            the activity key
     */
    public void setActivity(ActivityKey activity) {
        this.activity = activity;
    }

    /**
     * Gets the security token of the requesting user. Returns null if not set.
     * 
     * @return the token or null
     */
    public Token getToken() {
        return token;
    }

    /**
     * Sets the security token of the requesting user.
     * 
     * @param token
     *            the security token
     */
    public void setToken(Token token) {
        this.token = token;
    }
}
