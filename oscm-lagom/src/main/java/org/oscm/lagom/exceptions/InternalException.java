/*******************************************************************************
 *
 *  Copyright FUJITSU LIMITED 2016                                           
 *
 *  Creation Date: Jun 23, 2016                                                      
 *
 *******************************************************************************/

package org.oscm.lagom.exceptions;

import org.oscm.lagom.enums.Reason;
import org.oscm.lagom.interfaces.MessageKey;

/**
 * Service exception for unexpected internal errors.
 */
public class InternalException extends ServiceException {

    private static final long serialVersionUID = 1397618428214541396L;

    /**
     * Creates a new internal exception.
     *
     * @param messageKey the enum key for the message
     * @param values     the values for message placeholders
     */
    public InternalException(MessageKey messageKey, String... values) {
        super(messageKey, values);
    }

    /**
     * Creates a new internal exception.
     *
     * @param messageKey the enum key for the message
     * @param e          the causing exception.
     * @param values     the values for message placeholders
     */
    public InternalException(MessageKey messageKey, Throwable e, String... values) {
        super(messageKey, e, values);
    }

    @Override
    public Reason getReason() {
        return Reason.INTERNAL;
    }
}
