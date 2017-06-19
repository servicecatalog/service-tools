/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 19, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest.provider;

import org.glassfish.hk2.api.Factory;
import org.oscm.common.rest.RestContext;

/**
 * @author miethaner
 *
 */
public class RestContextProvider implements Factory<RestContext> {

    @Override
    public RestContext provide() {
        return new RestContext();
    }

    @Override
    public void dispose(RestContext instance) {
        // ignore
    }
}
