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

import com.industics.ilab.okr.security.JwtCredentials;
import com.industics.ilab.okr.security.SecurityContexts;
import com.industics.ilab.okr.security.token.TokenExtractor;
import com.industics.isword.common.security.AbstractAuthcFilter;
import com.industics.isword.common.security.Authenticator;
import com.industics.isword.common.security.CredentialsHolder;
import com.industics.isword.common.utils.RequestUtils;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Performs validation of provided JWT Token.
 */
public class JwtAuthcInterceptor extends HandlerInterceptorAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthcInterceptor.class);

    private final AuthcFilterAdapter authcFilterAdapter;
    private final TokenExtractor tokenExtractor;

    public JwtAuthcInterceptor(TokenExtractor tokenExtractor, List<Authenticator> authenticators) {
        this.tokenExtractor = tokenExtractor;
        this.authcFilterAdapter = new AuthcFilterAdapter(authenticators);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LOGGER.debug("preHandle - authenticating request uri '{}'", request.getRequestURI());
        String requestPath = RequestUtils.getRequestBaseUrl(request);
        authcFilterAdapter.doFilter(requestPath, tokenExtractor.extract(request));
        JwtCredentials securityContext = (JwtCredentials) CredentialsHolder.getCredentials();
        if (securityContext != null && securityContext.isRenewed()) {
            response.setHeader(SecurityContexts.HEADER_NAME_AUTHORIZATION_NEW, securityContext.getPrincipal().getRawToken().getToken());
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        LOGGER.debug("afterCompletion - cleanup credentials for uri '{}'", request.getRequestURI());
        authcFilterAdapter.cleanCredentials();
    }

    @SuppressWarnings("PMD.UselessOverridingMethod")
    @SuppressFBWarnings("SIC_INNER_SHOULD_BE_STATIC")
    class AuthcFilterAdapter extends AbstractAuthcFilter {
        private final List<Authenticator> authenticators;

        AuthcFilterAdapter(List<Authenticator> authenticators) {
            this.authenticators = authenticators;
            super.initialize();
        }

        @Override
        protected List<Authenticator> getAuthenticators() {
            return authenticators;
        }

        @Override
        protected void doFilter(String requestPath, String authorizationHeader) {
            super.doFilter(requestPath, authorizationHeader);
        }

        @Override
        protected void cleanCredentials() {
            super.cleanCredentials();
        }
    }
}
