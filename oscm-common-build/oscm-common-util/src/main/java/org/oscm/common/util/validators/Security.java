/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Aug 18, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.util.validators;

import java.util.UUID;

import org.oscm.common.interfaces.data.Token;
import org.oscm.common.interfaces.enums.Messages;
import org.oscm.common.interfaces.exceptions.SecurityException;
import org.oscm.common.interfaces.exceptions.ServiceException;

/**
 * Common class for validating permissions and security tokens.
 * 
 * @author miethaner
 */
public class Security {

    /**
     * Validates the user id from the given token with the given user id.
     * 
     * @param token
     *            the security token
     * @param userId
     *            the user id
     * @throws ServiceException
     */
    public static void validateUserId(Token token, UUID userId)
            throws ServiceException {
        if (token == null || token.getUserId() == null
                || userId != null && !token.getUserId().equals(userId)) {
            throw new SecurityException(Messages.ERROR_NOT_AUTHORIZED);
        }
    }

    /**
     * Validates the organization id from the given token with the given
     * organization id.
     * 
     * @param token
     *            the security token
     * @param organizationId
     *            the organization id
     * @throws ServiceException
     */
    public static void validateOrganizationId(Token token, UUID organizationId)
            throws ServiceException {
        if (token == null || token.getOrganizationId() == null
                || organizationId != null
                        && !token.getOrganizationId().equals(organizationId)) {
            throw new SecurityException(Messages.ERROR_NOT_AUTHORIZED);
        }
    }

    /**
     * Validates the tenant id from the given token with the given tenant id.
     * 
     * @param token
     *            the security token
     * @param tenantId
     *            the tenant id
     * @throws ServiceException
     */
    public static void validateTenantId(Token token, UUID tenantId)
            throws ServiceException {
        if (token == null || token.getTenantId() == null
                || tenantId != null && !token.getTenantId().equals(tenantId)) {
            throw new SecurityException(Messages.ERROR_NOT_AUTHORIZED);
        }
    }

    /**
     * Validates the given value with the restrictions from the given token.
     * 
     * @param token
     *            the security token
     * @param restriction
     *            the restriction key
     * @param value
     *            the restriction value
     * @throws ServiceException
     */
    public static void validateRestriction(Token token, String value)
            throws ServiceException {
        if (token == null || token.getRestrictions() == null
                || !token.getRestrictions().contains(value)) {
            throw new SecurityException(Messages.ERROR_NOT_AUTHORIZED);
        }
    }

    private Security() {
    }
}
