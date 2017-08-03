/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Jun 14, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.lagom.exceptions;

import org.oscm.lagom.enums.Reason;
import org.oscm.lagom.interfaces.MessageKey;

/**
 * Service exception for timeout events.
 * 
 * @author miethaner
 */
public class TimeoutException extends ServiceException {

    private static final long serialVersionUID = 3469213807765151538L;

    /**
     * Creates new timeout exception
     * 
     * @param messageKey
     *            the enum key for the message
     * @param values
     *            the values for message placeholders
     */
    public TimeoutException(MessageKey messageKey, String... values) {
        super(messageKey, values);
    }

    /**
     * Creates new timeout exception
     * 
     * @param messageKey
     *            the enum key for the message
     * @param e
     *            the causing exception.
     * @param values
     *            the values for message placeholders
     */
    public TimeoutException(MessageKey messageKey, Throwable e,
            String... values) {
        super(messageKey, e, values);
    }

    @Override
    public Reason getReason() {
        return Reason.TIMEOUT;
    }
}
