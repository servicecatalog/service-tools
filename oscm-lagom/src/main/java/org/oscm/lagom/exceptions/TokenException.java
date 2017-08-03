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
 * Service exception for security token events and errors (e.g. token missing)
 * 
 * @author miethaner
 */
public class TokenException extends ServiceException {

    private static final long serialVersionUID = -4196103934993229424L;

    /**
     * Creates new token exception
     * 
     * @param messageKey
     *            the enum key for the message
     * @param values
     *            the values for message placeholders
     */
    public TokenException(MessageKey messageKey, String... values) {
        super(messageKey, values);
    }

    /**
     * Creates new token exception
     * 
     * @param messageKey
     *            the enum key for the message
     * @param e
     *            the causing exception.
     * @param values
     *            the values for message placeholders
     */
    public TokenException(MessageKey messageKey, Throwable e,
            String... values) {
        super(messageKey, e, values);
    }

    @Override
    public Reason getReason() {
        return Reason.TOKEN;
    }
}
