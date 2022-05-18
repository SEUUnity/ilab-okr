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

public class UserTreeVO extends EntityObject {
    private static final long serialVersionUID = -3901387512925450201L;

    private String id;
    private String username;
    private String fullname;
    private String okrManager;
    private String mobile;
    private String email;
    private Integer sequence;
    private boolean dept;
    private boolean manager;
    private List<UserTreeVO> children;

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

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getOkrManager() {
        return okrManager;
    }

    public void setOkrManager(String okrManager) {
        this.okrManager = okrManager;
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

    public boolean isDept() {
        return dept;
    }

    public void setDept(boolean dept) {
        this.dept = dept;
    }

    public boolean isManager() {
        return manager;
    }

    public void setManager(boolean manager) {
        this.manager = manager;
    }

    public List<UserTreeVO> getChildren() {
        return children;
    }

    public void setChildren(List<UserTreeVO> children) {
        this.children = children;
    }
}
