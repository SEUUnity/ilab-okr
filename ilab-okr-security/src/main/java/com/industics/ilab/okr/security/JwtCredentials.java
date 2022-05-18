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

package com.industics.ilab.okr.security;

import com.industics.ilab.okr.security.token.JwtToken;
import com.industics.isword.common.security.Credentials;

public class JwtCredentials extends Credentials<JwtToken> {

    private static final String SCHEME = "jwt";
    private static final long serialVersionUID = 6177536743631473228L;

    private boolean renewed = false;

    public JwtCredentials() {
        super(SCHEME);
    }

    @SuppressWarnings("PMD.UselessOverridingMethod")
    @Override
    public void setPrincipal(JwtToken principal) {
        super.setPrincipal(principal);

        //TODO: fix permissions or authorities
        //Set<Permission> permissions = principal.getAuthorities();
        //if (permissions != null) {
        //    permissions.forEach(this::addPermission);
        //}
    }

    public boolean isRenewed() {
        return renewed;
    }

    public void setRenewed(boolean renewed) {
        this.renewed = renewed;
    }
}
