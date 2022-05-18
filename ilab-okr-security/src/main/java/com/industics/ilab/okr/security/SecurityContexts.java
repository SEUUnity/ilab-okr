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

package com.industics.ilab.okr.security;

import com.industics.ilab.okr.security.token.JwtToken;
import com.industics.isword.common.exception.UnauthorizedException;
import com.industics.isword.common.security.CredentialsHolder;

/**
 * Utility class for iUAA Security.
 */
public final class SecurityContexts {

    public static final String HEADER_NAME_AUTHORIZATION = "X-iLAB-Authorization";
    public static final String HEADER_NAME_AUTHORIZATION_NEW = "X-iLAB-Authorization-New";

    private SecurityContexts() {
    }

    /**
     * Get the login of the current user.
     *
     * @return the login of the current user
     */
    public static String getCurrentLoginInUser() throws UnauthorizedException {
        return getLoginUserContext().getUserId();
    }

    /**
     * Get the login context of the current user.
     *
     * @return the login context of the current user
     */
    public static JwtToken getLoginUserContext() throws UnauthorizedException {
        JwtCredentials securityContext = (JwtCredentials) CredentialsHolder.getCredentials();
        if (securityContext != null) {
            return securityContext.getPrincipal();
        }

        throw new UnauthorizedException();
    }

    /**
     * Constants for Spring Security authorities.
     */
    public static class Authorities {
        /**
         * app authority
         */
        public static final String APP_ID_PREFIX = "ROLE_APP_";
    }
}
