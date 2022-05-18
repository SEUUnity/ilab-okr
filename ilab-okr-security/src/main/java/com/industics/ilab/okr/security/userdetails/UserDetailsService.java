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

package com.industics.ilab.okr.security.userdetails;

import com.industics.ilab.okr.apiobjects.etype.ErrorTypes;
import com.industics.ilab.okr.dal.dao.repository.UserRepository;
import com.industics.ilab.okr.dal.dao.mapper.UserMapper;
import com.industics.ilab.okr.dal.entity.User;
import com.industics.ilab.okr.security.SecurityContexts;
import com.industics.ilab.okr.security.apiobjects.AuthcType;
import com.industics.ilab.okr.security.apiobjects.UserType;
import com.industics.ilab.okr.security.exception.TokenInvalidException;
import com.industics.ilab.okr.security.token.JwtToken;
import com.industics.isword.common.exception.ApiErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsService.class);

    private UserRepository userRepository;
    @Value("${isword.app-id:-1}")
    private String appId;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * @param username username
     * @return user details
     */
    public UserDetails loadUserByUsername(String username) {
        LOGGER.debug("loadUserByUsername: {}", username);
        User user = userRepository.findOneByUsername(username)
                .orElseThrow(() -> new ApiErrorException(ErrorTypes.USER_NOT_FOUND, username));
        return new OkrUserDetails(user, AuthcType.USERNAME_PASSWORD, getUserAuthorities());
    }

    /**
     * @param jwtToken user id token
     * @return user details
     */
    public UserDetails loadUserByToken(JwtToken jwtToken) {
        LOGGER.debug("loadUserByToken; userId: {}", jwtToken.getUserId());
        if (UserType.CORP == jwtToken.getUserType()) {
            User user = userRepository.findById(jwtToken.getUserId())
                    .orElseThrow(() -> new ApiErrorException(ErrorTypes.USER_NOT_FOUND, jwtToken.getUserId()));
            return new OkrUserDetails(user, jwtToken.getAuthcType(), getUserAuthorities());
        } else {
            throw new TokenInvalidException(jwtToken.getRawToken().getToken());
        }
    }

    private Set<String> getUserAuthorities() {
        Set<String> authorities = new HashSet<>();
        authorities.add(SecurityContexts.Authorities.APP_ID_PREFIX + appId);
        return authorities;
    }
}
