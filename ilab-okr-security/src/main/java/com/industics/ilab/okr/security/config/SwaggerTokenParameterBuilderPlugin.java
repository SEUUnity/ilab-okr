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

import com.industics.ilab.okr.security.SecurityContexts;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SwaggerTokenParameterBuilderPlugin implements OperationBuilderPlugin {
    private final java.util.function.Predicate<String> excludedPathSelector;
    private final java.util.function.Predicate<String> includedPathSelector;

    public SwaggerTokenParameterBuilderPlugin(SecurityProperties securityProperties) {
        this.excludedPathSelector = antPathPatternsToPredicate(securityProperties.getAuthc().getExcludePatterns());
        this.includedPathSelector = antPathPatternsToPredicate(securityProperties.getAuthc().getIncludePatterns());
    }

    @Override
    public void apply(OperationContext context) {
        String apiPathPattern = context.requestMappingPattern();
        if (!excludedPathSelector.test(apiPathPattern) && includedPathSelector.test(apiPathPattern)) {
            context.operationBuilder().parameters(Collections.singletonList(tokenParameter()));
        }
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }

    private Parameter tokenParameter() {
        return new ParameterBuilder()
                .name(SecurityContexts.HEADER_NAME_AUTHORIZATION)
                .description("token value")
                .defaultValue("Bearer XXX")
                .required(true)
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .build();
    }

    private java.util.function.Predicate<String> antPathPatternsToPredicate(List<String> antPathPatterns) {
        java.util.function.Predicate<String> excludedPathSelector;
        if (antPathPatterns != null && !antPathPatterns.isEmpty()) {
            List<Predicate<String>> predicates = new ArrayList<>();
            for (String pathPattern : antPathPatterns) {
                predicates.add(PathSelectors.ant(pathPattern));
            }
            excludedPathSelector = Predicates.or(predicates)::apply;
        } else {
            excludedPathSelector = s -> false;
        }
        return excludedPathSelector;
    }
}
