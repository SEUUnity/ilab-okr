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

import com.industics.ilab.okr.security.apiobjects.AuthcType;
import com.industics.ilab.okr.security.apiobjects.UserType;

import java.io.Serializable;
import java.util.Collection;

/**
 * The interface User details.
 *
 * @author stonehe
 * @date 30 /11/2017
 */
public interface UserDetails extends Serializable {
    /**
     * Gets user type.
     *
     * @return user type
     */
    UserType getUserType();

    /**
     * Gets authc type.
     *
     * @return authc type
     */
    AuthcType getAuthcType();

    /**
     * Returns the authorities granted to the user. Cannot return <code>null</code>.
     *
     * @return the authorities, sorted by natural key (never <code>null</code>)
     */
    Collection<String> getAuthorities();

    /**
     * Returns the user id used to authenticate the user. Cannot return <code>null</code>
     *
     * @return the user id (never <code>null</code>)
     */
    String getUserId();
}
