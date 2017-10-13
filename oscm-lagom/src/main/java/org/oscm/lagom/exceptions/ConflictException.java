/*******************************************************************************
 *
 *  Copyright FUJITSU LIMITED 2016                                           
 *
 *  Creation Date: Jun 24, 2016                                                      
 *
 *******************************************************************************/

package org.oscm.lagom.exceptions;

import org.oscm.lagom.enums.Reason;
import org.oscm.lagom.interfaces.MessageKey;

/**
 * Service exception for conflict events and errors (e.g. violation of a
 * constraint).
 */
public class ConflictException extends ServiceException {

    private static final long serialVersionUID = 5446563151469012266L;

    /**
     * Creates a new conflict exception
     *
     * @param messageKey the enum key for the message
     * @param values     the values for message placeholders
     */
    public ConflictException(MessageKey messageKey, String... values) {
        super(messageKey, values);
    }

    /**
     * Creates a new conflict exception
     *
     * @param messageKey the enum key for the message
     * @param e          the causing exception.
     * @param values     the values for message placeholders
     */
    public ConflictException(MessageKey messageKey, Throwable e, String... values) {
        super(messageKey, e, values);
    }

    @Override
    public Reason getReason() {
        return Reason.CONFLICT;
    }
}
