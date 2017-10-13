/*
 * ****************************************************************************
 *
 *    Copyright FUJITSU LIMITED 2017
 *
 *    Creation Date: 2017-08-04
 *
 * ****************************************************************************
 */

package org.oscm.lagom.filters;

import com.lightbend.lagom.javadsl.api.transport.RequestHeader;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.function.Function;

/**
 * Lagom filter class for basic authentication with HTTP(s).
 */
public class BasicAuthFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String HEADER_PREFIX = "Basic ";
    public static final String SEPARATOR = ":";

    /**
     * Provides a function for Lagom that adds a basic auth header. The payload is created from the
     * given user and password.
     *
     * @param user     the user name
     * @param password the user password
     * @return the function
     */
    public static Function<RequestHeader, RequestHeader> getFilter(String user,
        String password) {

        String combined = user + SEPARATOR + password;

        String encoded = Base64.getEncoder().encodeToString(combined.getBytes(
            StandardCharsets.UTF_8));

        String header = HEADER_PREFIX + encoded;

        return requestHeader ->
            requestHeader.withHeader(AUTHORIZATION_HEADER, header);
    }

    private BasicAuthFilter() {
    }
}
