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

import com.industics.ilab.okr.dal.entity.User;
import com.industics.ilab.okr.security.apiobjects.AuthcType;
import com.industics.ilab.okr.security.apiobjects.UserType;

import java.util.Collection;

public class OkrUserDetails implements UserDetails {

    private static final long serialVersionUID = 339227843908824795L;
    private final User user;
    private final AuthcType authcType;
    private final Collection<String> authorities;

    public OkrUserDetails(User user, AuthcType authcType, Collection<String> authorities) {
        this.user = user;
        this.authcType = authcType;
        this.authorities = authorities;
    }

    @Override
    public UserType getUserType() {
        return UserType.CORP;
    }

    @Override
    public AuthcType getAuthcType() {
        return authcType;
    }

    @Override
    public Collection<String> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUserId() {
        return user.getId();
    }

    public User getUser() {
        return user;
    }
}
