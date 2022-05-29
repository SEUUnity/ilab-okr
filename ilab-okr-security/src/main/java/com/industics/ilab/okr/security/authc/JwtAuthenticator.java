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

package com.industics.ilab.okr.security.authc;

import com.industics.ilab.okr.apiobjects.etype.ErrorTypes;
import com.industics.ilab.okr.dal.manager.UserManager;
import com.industics.ilab.okr.security.JwtCredentials;
import com.industics.ilab.okr.security.exception.SessionInvalidException;
import com.industics.ilab.okr.security.token.JwtToken;
import com.industics.ilab.okr.security.token.RawJwtToken;
import com.industics.ilab.okr.security.token.TokenServiceImpl;
import com.industics.ilab.okr.security.token.TokenVerifier;
import com.industics.ilab.okr.security.userdetails.UserDetails;
import com.industics.ilab.okr.security.userdetails.UserDetailsService;
import com.industics.isword.common.exception.ApiErrorException;
import com.industics.isword.common.security.Authenticator;
import com.industics.isword.common.security.Credentials;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

/**
 * Performs validation of provided JWT Token.
 */
@Component
public class JwtAuthenticator implements Authenticator {

    private TokenServiceImpl tokenService;
    private TokenVerifier tokenVerifier;
    private UserDetailsService userDetailsService;
    private UserManager userManager;

    @Autowired
    public void setUserManager(UserManager userManager){this.userManager=userManager;}

    @Override
    public String getAcceptAuthorization() {
        return "Bearer";
    }

    @Override
    public Credentials authenticate(String rawJwt) {
        JwtCredentials credentials = new JwtCredentials();
        RawJwtToken rawJwtToken = new RawJwtToken(rawJwt);
        JwtToken jwtToken = tokenService.parseJwtToken(rawJwtToken);
        verifySession(jwtToken);
        userManager.updateLastLogin(jwtToken.getUserId());
        long renewedInSeconds = jwtToken.getRenewedInSeconds()
                - Duration.between(jwtToken.getIssuedDate().toInstant(), Instant.now()).getSeconds();
        if (renewedInSeconds < 0) {
            String jti = jwtToken.getSid();
            if (!tokenVerifier.verify(jti)) {
                throw new ApiErrorException(ErrorTypes.TOKEN_INVALID, jwtToken.getRawToken().getToken());
            }

            UserDetails user = userDetailsService.loadUserByToken(jwtToken);
            jwtToken = tokenService.createJwtToken(user);
            credentials.setRenewed(true);
        }

        credentials.setPrincipal(jwtToken);
        return credentials;
    }

    private void verifySession(JwtToken token) {
        if (tokenVerifier != null && !tokenVerifier.verify(token.getSid())) {
            throw new SessionInvalidException(token.getSid());
        }
    }

    @Autowired
    public void setTokenService(TokenServiceImpl tokenService) {
        this.tokenService = tokenService;
    }

    @Autowired
    public void setTokenVerifier(TokenVerifier tokenVerifier) {
        this.tokenVerifier = tokenVerifier;
    }

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}

