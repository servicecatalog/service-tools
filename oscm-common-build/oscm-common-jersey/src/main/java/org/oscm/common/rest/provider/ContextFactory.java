/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 19, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest.provider;

import org.glassfish.hk2.api.Factory;
import org.oscm.common.rest.ServiceRequestContext;

/**
 * Context factory for service request contexts provides within request scope.
 * 
 * @author miethaner
 */
public class ContextFactory implements Factory<ServiceRequestContext> {

    @Override
    public ServiceRequestContext provide() {
        return new ServiceRequestContext();
    }

    @Override
    public void dispose(ServiceRequestContext instance) {
        // ignore
    }
}
