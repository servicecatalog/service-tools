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
 * Service exception for cache events (e.g. data not modified).
 */
public class CacheException extends ServiceException {

    private static final long serialVersionUID = -5288079064424707113L;

    /**
     * Creates a new cache exception.
     *
     * @param messageKey the enum key for the message
     * @param values     the values for message placeholders
     */
    public CacheException(MessageKey messageKey, String... values) {
        super(messageKey, values);
    }

    /**
     * Creates a new cache exception.
     *
     * @param messageKey the enum key for the message
     * @param e          the causing exception
     * @param values     the values for message placeholders
     */
    public CacheException(MessageKey messageKey, Throwable e, String... values) {
        super(messageKey, e, values);
    }

    @Override
    public Reason getReason() {
        return Reason.CACHE;
    }
}
