/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 24, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.kafka;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import org.oscm.common.interfaces.config.ServiceKey;
import org.oscm.common.interfaces.security.SecurityToken;

/**
 * Implementation for the security token.
 * 
 * @author miethaner
 */
public class InternalToken implements SecurityToken {

    @Override
    public Long getUserId() {
        return new Long(0);
    }

    @Override
    public Long getOrganizationId() {
        return new Long(0);
    }

    @Override
    public Long getTenantId() {
        return new Long(0);
    }

    @Override
    public Set<String> getRoles() {
        return Collections.unmodifiableSet(
                new TreeSet<>(Arrays.asList(ServiceKey.PRIVATE_ROLE)));
    }
}
