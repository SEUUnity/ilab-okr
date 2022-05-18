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

package com.industics.ilab.okr.web.endpoint;

import com.industics.ilab.okr.apiobjects.etype.ErrorTypes;
import com.industics.ilab.okr.security.SecurityContexts;
import com.industics.ilab.okr.security.token.JwtToken;

import com.industics.isword.common.exception.ApiErrorException;
import com.industics.isword.common.exception.UnauthorizedException;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URLEncoder;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

abstract class AbstractEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEndpoint.class);

    String getAuthenticatedUserIdMustMatchUrlUserId(String requestUserId) {
        String tokenUserId = getLoginUserContext().getUserId();
        if (!Objects.equals(tokenUserId, requestUserId)) {
            throw new ApiErrorException(ErrorTypes.TOKEN_MISMATCH_REQUEST_USER_ID, requestUserId);
        }
        return tokenUserId;
    }

    JwtToken getLoginUserContext() {
        JwtToken userContext = SecurityContexts.getLoginUserContext();
        if (userContext == null) {
            throw new UnauthorizedException();
        }
        return userContext;
    }

    ResponseEntity buildExcelResponse(Workbook workbook, String fileName, HttpServletResponse response) {
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            workbook.write(response.getOutputStream());
            response.flushBuffer();
            response.getOutputStream().close();
            workbook.close();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            LOGGER.warn("Warning", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("下载失败.");
        }
    }
}
