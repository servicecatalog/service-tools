/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: May 9, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;

import org.oscm.common.interfaces.data.ParameterType;
import org.oscm.common.interfaces.exceptions.ServiceException;
import org.oscm.common.interfaces.security.SecurityToken;
import org.oscm.common.util.validators.Validator;

/**
 * Super class for InjectParams
 * 
 * @author miethaner
 */
public abstract class RequestParameters implements ParameterType {

    public static final String PARAM_VERSION = "version";
    public static final String PARAM_TOKEN = "token";
    public static final String PARAM_ID = "id";
    public static final String PARAM_MATCH = "If-Match";
    public static final String PARAM_NONE_MATCH = "If-None-Match";
    public static final String PARAM_LIMIT = "limit";
    public static final String PARAM_OFFSET = "offset";

    public static final String AUTHORIZATION_PREFIX = "Bearer ";
    public static final String ETAG_WILDCARD = "*";

    private Integer version;

    @PathParam(PARAM_ID)
    private Long id;

    @HeaderParam(PARAM_MATCH)
    private String match;

    @HeaderParam(PARAM_NONE_MATCH)
    private String noneMatch;

    private Long etag;

    @QueryParam(PARAM_LIMIT)
    private Long limit;

    @QueryParam(PARAM_OFFSET)
    private Long offset;

    private SecurityToken securityToken;

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

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public String getNoneMatch() {
        return noneMatch;
    }

    public void setNoneMatch(String noneMatch) {
        this.noneMatch = noneMatch;
    }

    @Override
    public Long getETag() {
        return etag;
    }

    public void setETag(Long etag) {
        this.etag = etag;
    }

    public void validateAndCopyETag()
            throws WebApplicationException, ServiceException {

        if (noneMatch != null && !ETAG_WILDCARD.equals(noneMatch)) {
            etag = Validator.validateLong(PARAM_NONE_MATCH, noneMatch);
        }

        if (match != null && !ETAG_WILDCARD.equals(match)) {
            etag = Validator.validateLong(PARAM_NONE_MATCH, match);
        }
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

    /**
     * Validates the content and format of the parameters. Throws
     * BadRequestException if not valid.
     * 
     * Subclasses also need also to validate fields of the base class (except
     * resource id) that they are using.
     * 
     * @throws WebApplicationException
     */
    public abstract void validateParameters()
            throws WebApplicationException, ServiceException;

}
