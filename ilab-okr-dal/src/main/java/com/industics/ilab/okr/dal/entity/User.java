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

package com.industics.ilab.okr.dal.entity;

import com.industics.isword.common.jpa.entity.AbstractAuditableDeletableEntity;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@DynamicUpdate
@Table(name = "t_ilab_user")
public class User extends AbstractAuditableDeletableEntity {

    private static final long serialVersionUID = 1516588377925342396L;
    @Id
    @Column(name = "id", nullable = false, length = 32)
    private String id;
    @Column(name = "username", nullable = false, length = 32)
    private String username;
    @Column(name = "password", nullable = false, length = 64)
    private String password;
    @Column(name = "fullname", nullable = false, length = 8)
    private String fullname;
    @Column(name = "okr_manager", length = 32)
    private String okrManager;
    @Column(name = "mobile", nullable = false, length = 16)
    private String mobile;
    @Column(name = "email", length = 64)
    private String email;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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
}
