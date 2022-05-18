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

package com.industics.ilab.okr.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.Collections;
import java.util.List;

@ConfigurationProperties(prefix = "ilab.okr.security")
public class SecurityProperties {

    /**
     * Authentication configuration
     */
    @NestedConfigurationProperty
    private final AuthcProperties authc = new AuthcProperties();

    @NestedConfigurationProperty
    private final JwtProperties jwt = new JwtProperties();

    public AuthcProperties getAuthc() {
        return authc;
    }

    public JwtProperties getJwt() {
        return jwt;
    }

    public static class AuthcProperties {
        /**
         * Whether enable authentication filter.
         */
        private boolean enabled = true;

        private List<String> includePatterns = Collections.singletonList("/*/api/**");

        private List<String> excludePatterns = Collections.singletonList("/*/api/public/**");

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public List<String> getIncludePatterns() {
            return includePatterns;
        }

        public void setIncludePatterns(List<String> includePatterns) {
            this.includePatterns = includePatterns;
        }

        public List<String> getExcludePatterns() {
            return excludePatterns;
        }

        public void setExcludePatterns(List<String> excludePatterns) {
            this.excludePatterns = excludePatterns;
        }
    }

    public static class AuthzProperties {
        /**
         * Whether enable @Authorize interceptor
         */
        private boolean enabled = true;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    public static class JwtProperties {
        /**
         * Keystore file path
         */
        private String keystoreFile = "keystore.jks";

        /**
         * Keystore file path
         */
        private String publicKeyFile = "public.cer";

        /**
         * Default password for keystore file
         */
        private String keystorePassword = "Industics";

        /**
         * Alias name
         */
        private String alias = "uaa";

        /**
         * JWT Token will expire after this time (minute)
         * <p>
         * default to one week
         */
        private Integer expiredInMinutes = 7 * 24 * 60;

        /**
         * Token issuer.
         */
        private String issuer = "ind.iuaa";

        /**
         * JWT Token must be renewed after this time (minute)
         * <p>
         * default to 30 minutes
         */
        private Integer renewedInMinutes = 30;

        public String getKeystoreFile() {
            return keystoreFile;
        }

        public void setKeystoreFile(String keystoreFile) {
            this.keystoreFile = keystoreFile;
        }

        public String getPublicKeyFile() {
            return publicKeyFile;
        }

        public void setPublicKeyFile(String publicKeyFile) {
            this.publicKeyFile = publicKeyFile;
        }

        public String getKeystorePassword() {
            return keystorePassword;
        }

        public void setKeystorePassword(String keystorePassword) {
            this.keystorePassword = keystorePassword;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public Integer getExpiredInMinutes() {
            return expiredInMinutes;
        }

        public void setExpiredInMinutes(Integer expiredInMinutes) {
            this.expiredInMinutes = expiredInMinutes;
        }

        public String getIssuer() {
            return issuer;
        }

        public void setIssuer(String issuer) {
            this.issuer = issuer;
        }

        public Integer getRenewedInMinutes() {
            return renewedInMinutes;
        }

        public void setRenewedInMinutes(Integer renewedInMinutes) {
            this.renewedInMinutes = renewedInMinutes;
        }
    }
}
