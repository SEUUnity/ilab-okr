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

package com.industics.ilab.okr.apiobjects.user;

import com.industics.isword.common.entity.EntityObject;

import java.util.List;

public class UserVO extends EntityObject {

    private static final long serialVersionUID = -1737126483034000166L;
    private String id;
    private String username;
    private String fullname;
    private String mobile;
    private String email;
    private boolean manager;
//    private boolean special;
    private List<GroupVO> groups;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isManager() {
        return manager;
    }

    public void setManager(boolean manager) {
        this.manager = manager;
    }

//    public boolean isSpecial() {
//        return special;
//    }
//
//    public void setSpecial(boolean special) {
//        this.special = special;
//    }

    public List<GroupVO> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupVO> groups) {
        this.groups = groups;
    }
}
