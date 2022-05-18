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

package com.industics.ilab.okr.security.token.extractor;

import com.industics.ilab.okr.security.SecurityContexts;
import com.industics.ilab.okr.security.exception.TokenNotPresentException;
import com.industics.ilab.okr.security.token.TokenExtractor;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * An implementation of {@link TokenExtractor} extracts token from request header
 */
public class HeaderTokenExtractor implements TokenExtractor {
    private static final Logger LOGGER = LoggerFactory.getLogger(HeaderTokenExtractor.class);

    @Override
    public String extract(HttpServletRequest request) {
        String tokenHeaderValue = request.getHeader(SecurityContexts.HEADER_NAME_AUTHORIZATION);
        if (StringUtils.isBlank(tokenHeaderValue)) {
            LOGGER.debug("Header token value was blank, will extract token from request cookies.");
            throw new TokenNotPresentException();
        }

        return tokenHeaderValue;
    }
}
