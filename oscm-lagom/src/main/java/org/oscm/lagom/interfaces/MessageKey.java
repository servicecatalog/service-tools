/*
 * ****************************************************************************
 *
 *    Copyright FUJITSU LIMITED 2017
 *
 *    Creation Date: 2017-08-03
 *
 * ****************************************************************************
 */

package org.oscm.lagom.interfaces;

/**
 * Interface for enums that represent message keys.
 *
 * @author miethaner
 */
public interface MessageKey {

    /**
     * Gets the message id.
     *
     * @return the message id.
     */
    public Integer getCode();

    /**
     * Gets the corresponding message.
     *
     * @param values the values to replace placeholders with
     * @return the message
     */
    public String getMessage(String... values);
}
