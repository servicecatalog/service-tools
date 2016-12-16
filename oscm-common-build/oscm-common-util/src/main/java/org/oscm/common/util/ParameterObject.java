/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Dec 12, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.util;

import org.oscm.common.interfaces.data.ParameterType;
import org.oscm.common.interfaces.security.SecurityToken;

/**
 * Superclass for parameter objects.
 * 
 * @author miethaner
 */
public class ParameterObject implements ParameterType {

    private Long id;

    private Long etag;

    private Long limit;

    private Long offset;

    private SecurityToken securityToken;

    public ParameterObject() {
    }

    public ParameterObject(ParameterType params) {
        if (params == null) {
            return;
        }

        this.id = params.getId();
        this.etag = params.getETag();
        this.limit = params.getLimit();
        this.offset = params.getOffset();
        this.securityToken = params.getSecurityToken();
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
        return etag;
    }

    public void setETag(Long etag) {
        this.etag = etag;
    }

    @Override
    public Long getLimit() {
        return limit;
    }

    public void setLimit(Long limit) {
        this.limit = limit;
    }

    @Override
    public Long getOffset() {
        return offset;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }

    @Override
    public SecurityToken getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(SecurityToken securityToken) {
        this.securityToken = securityToken;
    }
}
