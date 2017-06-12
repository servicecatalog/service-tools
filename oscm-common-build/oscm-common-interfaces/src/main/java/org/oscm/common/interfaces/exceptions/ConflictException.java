/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jun 24, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.exceptions;

import org.oscm.common.interfaces.enums.Reason;
import org.oscm.common.interfaces.keys.MessageKey;

/**
 * Component exception for conflict events and errors (e.g. violation of a
 * constraint)
 * 
 * @author miethaner
 */
public class ConflictException extends ServiceException {

    private static final long serialVersionUID = 5446563151469012266L;

    /**
     * Creates new conflict exception
     * 
     * @param messageKey
     *            the enum key for the message
     * @param values
     *            the values for message placeholders
     */
    public ConflictException(MessageKey messageKey, String... values) {
        super(messageKey, values);
    }

    /**
     * Creates new conflict exception
     * 
     * @param messageKey
     *            the enum key for the message
     * @param e
     *            the causing exception.
     * @param values
     *            the values for message placeholders
     */
    public ConflictException(MessageKey messageKey, Throwable e,
            String... values) {
        super(messageKey, e, values);
    }

    @Override
    public Reason getReason() {
        return Reason.CONFLICT;
    }
}
