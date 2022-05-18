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

package com.industics.ilab.okr.apiobjects.etype;

import com.industics.isword.common.entity.ApiError;

@SuppressWarnings("MethodParamPad")
public enum ErrorTypes implements ApiError {
    // @formatter:off

    RESOURCE_NOT_FOUND                                  (404, "errors.com.industics.resource.not_found", "{0} {1} not found"),
    RESOURCE_ALREADY_EXISTS                             (400, "errors.com.industics.resource.already_exists", "{0} {1} already exists"),
    RESOURCE_OPERATION_FAILED                           (400, "errors.com.industics.resource.operation.failed", "{0} {1} failed."),
    RESOURCE_OPERATION_FORBIDDEN                        (401, "errors.com.industics.resource.operation.forbidden", "{0} opeartion {1} forbidden."),

    SERIAL_NUMBER_DUPLICATED                            (400, "errors.com.industics.serial_number.duplicated", "序列号'{0}'生成失败."),

    SESSION_INVALID                                     (401, "errors.com.industics.session.invalid", "Session '{0}' is invalid."),

    TOKEN_EXPIRED                                       (401, "errors.com.industics.token.expired", "Token '{0}' has expired."),
    TOKEN_INVALID                                       (401, "errors.com.industics.token.invalid", "Token '{0}' is invalid."),
    TOKEN_NOT_PRESENT                                   (401, "errors.com.industics.token.not_present", "Token is not present in request."),
    TOKEN_MISMATCH_REQUEST_USER_ID                      (401, "errors.com.industics.token_mismatch_user", "Token mismatch user '{}' in request."),

    USER_PASSWORD_INCORRECT                             (400, "errors.com.industics.user.incorrect_password", "Password was incorrect."),
    USER_NOT_FOUND                                      (404, "errors.com.industics.user.not_found", "User '{0}' was not found."),
    GROUP_NOT_FOUND                                     (404, "errors.com.industics.user.group.not_found", "Group '{0}' was not found."),
    GROUP_ALREADY_EXISTS                                (400, "errors.com.industics.user.group.already_exists", "用户组 {0} 已存在"),
    USER_ALREADY_IN_GROUP                               (400, "errors.com.industics.user.already_in_group", "用户已经在当前组中"),
    USER_NOT_IN_GROUP                                   (404, "errors.com.industics.user.not_in_group", "User '{0}' was not in Group '{1}'."),
    FILE_OPERATION_FAILED                               (400, "errors.com.industics.ibrain.isim.simissue.file.file_operation_failed", "文件操作失败"),

    REVIEW_VERSION_NOT_EXISTS                           (400, "errors.com.industics.review.review_version_not_exists", "review version不存在");

    // @formatter:on

    private final int status;
    private final String error;
    private final String message;

    ErrorTypes(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
    }

    @Override
    public int status() {
        return status;
    }

    @Override
    public String code() {
        return error;
    }

    @Override
    public String message() {
        return message;
    }
}
