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

package com.industics.ilab.okr.security.aware;

import com.industics.ilab.okr.security.JwtCredentials;
import com.industics.ilab.okr.security.token.JwtToken;
import com.industics.isword.common.security.CredentialsHolder;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * Implementation of AuditorAware based on Spring Security.
 */
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    private static final String DEFAULT_OPERATOR_ID = "99999999999999999999999999999999";

    @Override
    public Optional<String> getCurrentAuditor() {
        JwtToken jwtToken = null;
        if (CredentialsHolder.getCredentials() != null) {
            jwtToken = ((JwtCredentials) CredentialsHolder.getCredentials()).getPrincipal();
        }
        return Optional.of(jwtToken != null ? jwtToken.getUserId() : DEFAULT_OPERATOR_ID);
    }
}
