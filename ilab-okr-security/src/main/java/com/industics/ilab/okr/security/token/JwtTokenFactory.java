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

import com.industics.ilab.okr.security.apiobjects.AuthcType;
import com.industics.ilab.okr.security.apiobjects.UserType;
import com.industics.ilab.okr.security.exception.TokenExpiredException;
import com.industics.ilab.okr.security.exception.TokenInvalidException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Key;
import java.security.KeyPair;
import java.util.List;

/**
 * Factory method for issuing new JWT Tokens.
 */
public class JwtTokenFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenFactory.class);

    private static final String CLAIM_NAME_RENEWED_IN_SECONDS = "renewed_in_seconds";
    private static final String CLAIM_NAME_USER_TYPE = "user_type";
    private static final String CLAIM_NAME_AUTHC_TYPE = "authc_type";
    private static final String CLAIM_NAME_AUTHORITIES = "authorities";

    /**
     * Create id jwt token
     *
     * @param jwtToken user context
     * @return access id token
     */
    public RawJwtToken createJwtToken(JwtToken jwtToken, KeyPair keyPair) {
        if (jwtToken.getAuthorities() == null) {
            throw new IllegalArgumentException("User doesn't have any privileges");
        }

        Claims claims = Jwts.claims().setSubject(jwtToken.getUserId());
        claims.put(CLAIM_NAME_RENEWED_IN_SECONDS, jwtToken.getRenewedInSeconds());
        claims.put(CLAIM_NAME_USER_TYPE, jwtToken.getUserType());
        claims.put(CLAIM_NAME_AUTHC_TYPE, jwtToken.getAuthcType().name());
        claims.put(CLAIM_NAME_AUTHORITIES, jwtToken.getAuthorities());

        String token = Jwts.builder()
                .setClaims(claims)
                .setId(jwtToken.getSid())
                .setIssuer(jwtToken.getIssuer())
                .setIssuedAt(jwtToken.getIssuedDate())
                .setExpiration(jwtToken.getExpirationDate())
                .signWith(SignatureAlgorithm.RS512, keyPair.getPrivate())
                .compact();

        return new RawJwtToken(token);
    }

    @SuppressWarnings("unchecked")
    public JwtToken parseJwtToken(RawJwtToken rawJwtToken, Key signingKey) throws TokenInvalidException, TokenExpiredException {
        Jws<Claims> jwsClaims = parseClaims(rawJwtToken.getToken(), signingKey);
        Claims claims = jwsClaims.getBody();
        String sid = claims.getId();
        String issuer = claims.getIssuer();
        String userId = claims.getSubject();
        UserType userType = UserType.CORP;
        if (StringUtils.isNotBlank(claims.get(CLAIM_NAME_USER_TYPE, String.class))) {
            userType = UserType.valueOf(claims.get(CLAIM_NAME_USER_TYPE, String.class));
        }
        AuthcType authcType = AuthcType.valueOf(claims.get(CLAIM_NAME_AUTHC_TYPE, String.class));
        List<String> authorities = claims.get(CLAIM_NAME_AUTHORITIES, List.class);
        Integer renewedInSeconds = claims.get(CLAIM_NAME_RENEWED_IN_SECONDS, Integer.class);

        JwtToken jwtToken = new JwtToken(rawJwtToken);
        jwtToken.setIssuer(issuer);
        jwtToken.setSid(sid);
        jwtToken.setUserId(userId);
        jwtToken.setIssuedDate(claims.getIssuedAt());
        jwtToken.setExpirationDate(claims.getExpiration());
        jwtToken.setRenewedInSeconds(renewedInSeconds);
        jwtToken.setUserType(userType);
        jwtToken.setAuthcType(authcType);
        jwtToken.setAuthorities(authorities);
        return jwtToken;
    }


    /**
     * Parses and validates JWT Token signature.
     *
     * @throws TokenInvalidException if jwt was invalid
     * @throws TokenExpiredException if jwt was expired
     */
    private Jws<Claims> parseClaims(String token, Key signingKey) {
        try {
            return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {
            LOGGER.error("Invalid JWT Token.", ex);
            throw new TokenInvalidException(token);
        } catch (ExpiredJwtException expiredEx) {
            LOGGER.info("JWT Token is expired.", expiredEx);
            throw new TokenExpiredException(token);
        }
    }
}
