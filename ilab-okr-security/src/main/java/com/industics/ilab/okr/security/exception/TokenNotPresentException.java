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

package com.industics.ilab.okr.security.exception;

import com.industics.ilab.okr.apiobjects.etype.ErrorTypes;
import com.industics.isword.common.exception.ApiErrorException;

public class TokenNotPresentException extends ApiErrorException {

    private static final long serialVersionUID = 7895308258272531598L;

    public TokenNotPresentException() {
        super(ErrorTypes.TOKEN_NOT_PRESENT);
    }
}
