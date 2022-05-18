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

package com.industics.ilab.okr.security.endpoint;

import com.industics.ilab.okr.security.SecurityContexts;
import com.industics.ilab.okr.security.token.JwtToken;

import io.swagger.annotations.Api;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint for retrieving login user details.
 */
@Api(tags = "TOKEN", value = "TOKEN API")
@RestController
@RequestMapping("/v2/api")
public class TokenEndpoint {

    @GetMapping(value = "/token-me", produces = {MediaType.APPLICATION_JSON_VALUE})
    public JwtToken get() {
        return SecurityContexts.getLoginUserContext();
    }
}
