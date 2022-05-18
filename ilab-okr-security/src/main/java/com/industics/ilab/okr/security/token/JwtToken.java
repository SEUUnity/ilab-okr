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

package com.industics.ilab.okr.security.token;

import com.industics.ilab.okr.security.apiobjects.AuthcType;
import com.industics.ilab.okr.security.apiobjects.UserType;
import com.industics.isword.common.entity.EntityObject;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import javax.validation.constraints.NotNull;

@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class JwtToken extends EntityObject {

    private static final long serialVersionUID = -3513023455139099495L;
    @NotNull
    private RawJwtToken rawToken;

    /**
     * The rawToken issuer
     */
    @NotNull
    private String issuer;

    /**
     * The session id related to current rawToken
     */
    @NotNull
    private String sid;

    /**
     * The user id related to current rawToken
     */
    @NotNull
    private String userId;
    @NotNull
    private Date issuedDate;
    @NotNull
    private Date expirationDate;

    private Integer renewedInSeconds = 0;

    private UserType userType;
    private AuthcType authcType;
    private Collection<String> authorities = Collections.emptyList();

    public JwtToken() {
        // for new created
    }

    public JwtToken(RawJwtToken rawToken) {
        // for parse from raw token
        this.rawToken = rawToken;
    }

    public RawJwtToken getRawToken() {
        return rawToken;
    }

    public void setRawToken(RawJwtToken rawToken) {
        this.rawToken = rawToken;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(Date issuedDate) {
        this.issuedDate = issuedDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Integer getRenewedInSeconds() {
        return renewedInSeconds;
    }

    public void setRenewedInSeconds(Integer renewedInSeconds) {
        this.renewedInSeconds = renewedInSeconds;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public AuthcType getAuthcType() {
        return authcType;
    }

    public void setAuthcType(AuthcType authcType) {
        this.authcType = authcType;
    }

    public Collection<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<String> authorities) {
        this.authorities = authorities;
    }
}
