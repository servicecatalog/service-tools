/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2017                                           
 *                                                                                                                                 
 *  Creation Date: Feb 2, 2017                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.rest;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAKey;
import java.util.Date;

import org.oscm.common.interfaces.enums.Messages;
import org.oscm.common.interfaces.exceptions.ServiceException;
import org.oscm.common.interfaces.exceptions.TokenException;

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

    private static final String ALGORITHM = "RSA";
    private static final String ISSUER = "OSCM";

    private static TokenManager tm;

    /**
     * Returns the singleton instance of the token manager.
     * 
     * @return the token manager
     */
    public static TokenManager getInstance() {
        if (tm == null) {
            throw new RuntimeException("Token manager not initialized");
        }

        return tm;
    }

    /**
     * Initializes the token manager with the keystore information and reads the
     * encryption/decryption keys corresponding to the given alias.
     * 
     * @param keystoreLoc
     *            the location of the keystore
     * @param keystorePwd
     *            the password of the keystore
     * @param keystoreAlias
     *            the alias of the private key in the keystore
     */
    public static void init(String keystoreLoc, String keystorePwd,
            String keystoreAlias) {
        tm = new TokenManager(keystoreLoc, keystorePwd, keystoreAlias);
    }

    private PrivateKey privateKey;
    private PublicKey publicKey;
    private JWTVerifier verifier;

    private TokenManager(String keystoreLoc, String keystorePwd,
            String keystoreAlias) {

        InputStream is = null;
        try {
            is = new FileInputStream(keystoreLoc);
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(is, keystorePwd.toCharArray());
            is.close();

            privateKey = (PrivateKey) keystore.getKey(keystoreAlias,
                    keystorePwd.toCharArray());

            if (privateKey == null
                    || !ALGORITHM.equals(privateKey.getAlgorithm())) {
                throw new RuntimeException(
                        "Unable to load correct private key from keystore");
            }

            Certificate cert = keystore.getCertificate(keystoreAlias);

            if (cert == null || cert.getPublicKey() == null
                    || !ALGORITHM.equals(cert.getPublicKey().getAlgorithm())) {
                throw new RuntimeException(
                        "Unable to load correct public key form truststore");
            }

            publicKey = cert.getPublicKey();

            verifier = JWT.require(Algorithm.RSA256((RSAKey) publicKey))
                    .withIssuer(ISSUER).build();

        } catch (KeyStoreException | NoSuchAlgorithmException
                | CertificateException | IOException
                | UnrecoverableKeyException e) {
            throw new RuntimeException("Unable to initialize token manager", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e1) {
                    // ignore
                }
            }
        }
    }

    /**
     * Create a new JSON web token with the given expiration time from the given
     * service token. Also encodes the jwt and sign it with the configured key.
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
                .withClaim(Token.FIELD_USER_ID, token.getUserIdString())
                .withClaim(Token.FIELD_ORGANIZATION_ID,
                        token.getOrganizationIdString())
                .withClaim(Token.FIELD_TENANT_ID, token.getTenantIdString())
                .withArrayClaim(Token.FIELD_ROLES, token.getRolesArray())
                .sign(Algorithm.RSA256((RSAKey) privateKey));
    }

    /**
     * Decrypts and decodes the JSON web token and verifes it.
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

            return token;
        } catch (JWTVerificationException e) {
            throw new TokenException(Messages.NOT_AUTHENTICATED, e);
        }
    }
}
