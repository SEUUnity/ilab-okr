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

package com.industics.ilab.okr.security.config;

import com.industics.ilab.okr.security.authc.JwtAuthcInterceptor;
import com.industics.ilab.okr.security.token.TokenExtractor;
import com.industics.isword.common.security.Authenticator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
@Import(SecurityConfiguration.class)
public class WebSecurityConfiguration extends WebMvcConfigurerAdapter {

    private SecurityProperties securityProperties;
    private TokenExtractor tokenExtractor;
    private List<Authenticator> authenticators;
    @Value("${isword.app-id:-1}")
    private int appId;

    @Autowired
    public void setSecurityProperties(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Autowired
    public void setTokenExtractor(TokenExtractor tokenExtractor) {
        this.tokenExtractor = tokenExtractor;
    }

    @Autowired(required = false)
    public void setAuthenticators(List<Authenticator> authenticators) {
        this.authenticators = authenticators;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtAuthcInterceptor(tokenExtractor, authenticators))
                .addPathPatterns(securityProperties.getAuthc().getIncludePatterns().toArray(new String[]{}))
                .excludePathPatterns(securityProperties.getAuthc().getExcludePatterns().toArray(new String[]{}));
    }
}
