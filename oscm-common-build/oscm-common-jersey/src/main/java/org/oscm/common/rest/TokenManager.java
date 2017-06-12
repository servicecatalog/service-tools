/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Feb 2, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.oscm.common.interfaces.data.Token;
import org.oscm.common.interfaces.enums.Messages;
import org.oscm.common.interfaces.exceptions.ServiceException;
import org.oscm.common.interfaces.exceptions.TokenException;
import org.oscm.common.util.ServiceConfiguration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * Singleton class to manage tokens for authentication.
 * 
 * @author miethaner
 */
public class TokenManager {

    private static final String ISSUER = "OSCM";

    private static TokenManager tm;

    /**
     * Returns the singleton instance of the token manager.
     * 
     * @return the token manager
     */
    public static TokenManager getInstance() {
        if (tm == null) {
            tm = new TokenManager();
        }

        return tm;
    }

    private String secret;
    private JWTVerifier verifier;

    private TokenManager() {

        this.secret = ServiceConfiguration.getInstance()
                .getConfig(JerseyConfig.JERSEY_TOKEN_SECRET);

        verifier = JWT
                .require(Algorithm
                        .HMAC512(secret.getBytes(StandardCharsets.UTF_8)))
                .withIssuer(ISSUER).build();
    }

    /**
     * Create a new JSON web token with the given expiration time from the given
     * service token. Also encodes the jwt and sign it with the configured
     * secret.
     * 
     * @param token
     *            the service token
     * @param expirationTime
     *            the expiration time for the created token
     * @return the encoded and signed token
     */
    public String createAndEncryptToken(Token token, long expirationTime) {

        return JWT.create().withIssuer(ISSUER)
                .withExpiresAt(
                        new Date(System.currentTimeMillis() + expirationTime))
                .withClaim(Token.FIELD_USER_ID, token.getUserIdAsString())
                .withClaim(Token.FIELD_ORGANIZATION_ID,
                        token.getOrganizationIdAsString())
                .withClaim(Token.FIELD_TENANT_ID, token.getTenantIdAsString())
                .withArrayClaim(Token.FIELD_ROLES, token.getRolesAsArray())
                .withArrayClaim(Token.FIELD_RESTRICTIONS,
                        token.getRestrictionsAsArray())
                .sign(Algorithm
                        .HMAC512(secret.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * Decodes and verifies the given JSON web token.
     * 
     * @param tokenString
     *            the encoded token
     * @return the service token
     * @throws ServiceException
     */
    public Token decryptAndVerifyToken(String tokenString)
            throws ServiceException {

        try {
            DecodedJWT jwt = verifier.verify(tokenString);

            Token token = new Token();
            token.setUserId(jwt.getClaim(Token.FIELD_USER_ID).asString());
            token.setOrganizationId(
                    jwt.getClaim(Token.FIELD_ORGANIZATION_ID).asString());
            token.setTenantId(jwt.getClaim(Token.FIELD_TENANT_ID).asString());
            token.setRoles(
                    jwt.getClaim(Token.FIELD_ROLES).asArray(String.class));
            token.setRestrictions(jwt.getClaim(Token.FIELD_RESTRICTIONS)
                    .asArray(String.class));

            return token;
        } catch (JWTVerificationException e) {
            throw new TokenException(Messages.NOT_AUTHENTICATED, e);
        }
    }
}
