/*
 * Copyright (c) 2018 Industics Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.industics.ilab.okr.security.token;

import com.industics.ilab.okr.security.SecurityContexts;
import com.industics.ilab.okr.security.apiobjects.AuthcType;
import com.industics.ilab.okr.security.apiobjects.UserType;
import com.industics.ilab.okr.security.config.SecurityProperties;
import com.industics.ilab.okr.security.exception.TokenExpiredException;
import com.industics.ilab.okr.security.exception.TokenInvalidException;
import com.industics.ilab.okr.security.userdetails.UserDetails;
import com.industics.isword.common.utils.UniqueString;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.RSAPublicKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;

/**
 * Factory method for issuing new JWT Tokens.
 */
@Service
public class TokenServiceImpl implements TokenService {

    private final JwtTokenFactory tokenFactory;
    private final SecurityProperties securityProperties;
    private final KeyPair keyPair;

    @Autowired
    public TokenServiceImpl(JwtTokenFactory tokenFactory, SecurityProperties securityProperties) {
        this.tokenFactory = tokenFactory;
        this.securityProperties = securityProperties;

        this.keyPair = new KeyStoreKeyFactory(new ClassPathResource(securityProperties.getJwt().getKeystoreFile()),
                securityProperties.getJwt().getKeystorePassword().toCharArray()).getKeyPair(securityProperties.getJwt().getAlias());
    }

    public JwtToken createJwtToken(Map<String,Object>map) {
        if (map.getOrDefault("permission","").toString().equals("")) {
            throw new IllegalArgumentException("User doesn't have any privileges");
        }

        Instant currentTime = Instant.now();
        JwtToken jwtToken = new JwtToken();
        jwtToken.setIssuer(securityProperties.getJwt().getIssuer());
        jwtToken.setSid(UniqueString.uuidUniqueString());
        jwtToken.setUserId(map.getOrDefault("admin_id","").toString());
        jwtToken.setIssuedDate(Date.from(currentTime));
        jwtToken.setExpirationDate(Date.from(currentTime.plus(securityProperties.getJwt().getExpiredInMinutes(), ChronoUnit.MINUTES)));
        jwtToken.setRenewedInSeconds(securityProperties.getJwt().getRenewedInMinutes() * 60);
        UserType userType=UserType.CORP;
        AuthcType authcType=AuthcType.USERNAME_PASSWORD;
        Collection<String>authorities=new HashSet<>();
        authorities.add("1");
        jwtToken.setUserType(userType);
        jwtToken.setAuthcType(authcType);
        jwtToken.setAuthorities(authorities);
        jwtToken.setRawToken(tokenFactory.createJwtToken(jwtToken, keyPair));

        return jwtToken;
    }
    public JwtToken createJwtToken(UserDetails userDetails) {
        if (userDetails.getAuthorities() == null) {
            throw new IllegalArgumentException("User doesn't have any privileges");
        }

        Instant currentTime = Instant.now();
        JwtToken jwtToken = new JwtToken();
        jwtToken.setIssuer(securityProperties.getJwt().getIssuer());
        jwtToken.setSid(UniqueString.uuidUniqueString());
        jwtToken.setUserId(userDetails.getUserId());
        jwtToken.setIssuedDate(Date.from(currentTime));
        jwtToken.setExpirationDate(Date.from(currentTime.plus(securityProperties.getJwt().getExpiredInMinutes(), ChronoUnit.MINUTES)));
        jwtToken.setRenewedInSeconds(securityProperties.getJwt().getRenewedInMinutes() * 60);
        jwtToken.setUserType(userDetails.getUserType());
        jwtToken.setAuthcType(userDetails.getAuthcType());
        jwtToken.setAuthorities(userDetails.getAuthorities());
        jwtToken.setRawToken(tokenFactory.createJwtToken(jwtToken, keyPair));

        return jwtToken;
    }

    @Override
    @SuppressWarnings("unchecked")
    public JwtToken parseJwtToken(RawJwtToken rawAccessToken) throws TokenInvalidException, TokenExpiredException {
        return tokenFactory.parseJwtToken(rawAccessToken, keyPair.getPublic());
    }

    private static class KeyStoreKeyFactory {
        private Resource resource;
        private char[] password;
        private KeyStore store;
        private final Object lock = new Object();

        KeyStoreKeyFactory(Resource resource, char[] password) {
            this.resource = resource;
            this.password = password;
        }

        KeyPair getKeyPair(String alias) {
            return getKeyPair(alias, password);
        }

        KeyPair getKeyPair(String alias, char[] password) {
            try {
                synchronized (lock) {
                    if (store == null) {
                        synchronized (lock) {
                            store = KeyStore.getInstance("jks");
                            store.load(resource.getInputStream(), this.password);
                        }
                    }
                }
                RSAPrivateCrtKey key = (RSAPrivateCrtKey) store.getKey(alias, password);
                RSAPublicKeySpec spec = new RSAPublicKeySpec(key.getModulus(), key.getPublicExponent());
                PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(spec);
                return new KeyPair(publicKey, key);
            } catch (Exception e) {
                throw new IllegalStateException("Cannot load keys from store: " + resource, e);
            }
        }
    }
}
