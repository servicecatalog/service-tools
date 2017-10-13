/*******************************************************************************
 *
 *  Copyright FUJITSU LIMITED 2016                                           
 *
 *  Creation Date: Jul 13, 2016                                                      
 *
 *******************************************************************************/

package org.oscm.lagom.exceptions;

import org.oscm.lagom.enums.Reason;
import org.oscm.lagom.interfaces.MessageKey;

/**
 * Service exception for security events and errors (e.g. permissions).
 */
public class SecurityException extends ServiceException {

    private static final long serialVersionUID = 6780718172186488863L;

    /**
     * Creates a new security exception.
     *
     * @param messageKey the enum key for the message
     * @param values     the values for message placeholders
     */
    public SecurityException(MessageKey messageKey, String... values) {
        super(messageKey, values);
    }

    /**
     * Creates a new security exception.
     *
     * @param messageKey the enum key for the message
     * @param e          the causing exception.
     * @param values     the values for message placeholders
     */
    public SecurityException(MessageKey messageKey, Throwable e, String... values) {
        super(messageKey, e, values);
    }

    @Override
    public Reason getReason() {
        return Reason.SECURITY;
    }
}
