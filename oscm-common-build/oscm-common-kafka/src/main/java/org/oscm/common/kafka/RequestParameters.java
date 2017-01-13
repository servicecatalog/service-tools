/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: May 9, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.kafka;

import java.util.Map;

import org.oscm.common.interfaces.data.ParameterType;
import org.oscm.common.interfaces.exceptions.ServiceException;
import org.oscm.common.interfaces.security.SecurityToken;
import org.oscm.common.util.Validator;

/**
 * Super class for event based parameters
 * 
 * @author miethaner
 */
public abstract class RequestParameters implements ParameterType {

    public static final String PARAM_ID = "id";

    private Integer version;
    private Long id;

    public RequestParameters(Integer version, Map<String, String> parameters)
            throws ServiceException {
        this.version = version;

        if (parameters == null) {
            return;
        }

        this.id = Validator.validateLong(PROPERTY_ID, parameters.get(PARAM_ID));
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getETag() {
        return null;
    }

    @Override
    public Long getLimit() {
        return null;
    }

    @Override
    public Long getOffset() {
        return null;
    }

    @Override
    public SecurityToken getSecurityToken() {
        return new InternalToken();
    }

}
