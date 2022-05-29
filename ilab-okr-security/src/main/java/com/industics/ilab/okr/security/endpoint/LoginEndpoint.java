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

package com.industics.ilab.okr.security.endpoint;

import com.industics.ilab.okr.apiobjects.etype.ErrorTypes;
import com.industics.ilab.okr.dal.manager.UserManager;
import com.industics.ilab.okr.security.apiobjects.PasswordLoginRequest;
import com.industics.ilab.okr.security.apiobjects.UserType;
import com.industics.ilab.okr.security.token.JwtToken;
import com.industics.ilab.okr.security.token.RawJwtToken;
import com.industics.ilab.okr.security.token.TokenServiceImpl;
import com.industics.ilab.okr.security.userdetails.OkrUserDetails;
import com.industics.ilab.okr.security.userdetails.UserDetailsService;
import com.industics.ilab.okr.security.utils.Result;
import com.industics.ilab.okr.security.utils.TokenUtils;
import com.industics.isword.common.exception.ApiErrorException;
import com.industics.isword.common.exception.ForbiddenException;
import com.atlassian.security.password.DefaultPasswordEncoder;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * @author stonehe
 * @date 19/09/2017
 */
@RestController
@Api(tags = "LOGIN", value = "登录API")
@RequestMapping("/v2/api/public")
@Validated
public class LoginEndpoint {

    private static Logger LOGGER = LoggerFactory.getLogger(LoginEndpoint.class);
    private UserDetailsService userDetailsService;
    private TokenServiceImpl tokenService;
    private UserManager userManager;

    @ApiOperation(tags = "PUBLIC", value = "用户名密码登录")
    @PostMapping(value = "/login-with-password",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String, Object> loginWithPassword(@RequestBody @NotNull @Valid PasswordLoginRequest loginRequest) {
        if (UserType.CORP == loginRequest.getUserType()) {
            Map<String,Object> map=userManager.getAdminByUsername(loginRequest.getUsername());
            if(map==null){
                map=new HashMap<>();
            }
            System.out.println(map);
            if (!DefaultPasswordEncoder.getDefaultInstance().isValidPassword(loginRequest.getPassword(), map.getOrDefault("password","").toString())) {
                throw new ApiErrorException(ErrorTypes.USER_PASSWORD_INCORRECT);
            }
            JwtToken jwtToken = tokenService.createJwtToken(map);

//            OkrUserDetails userDetails = (OkrUserDetails) userDetailsService.loadUserByUsername(loginRequest.getUsername());
//            if (!DefaultPasswordEncoder.getDefaultInstance().isValidPassword(loginRequest.getPassword(), userDetails.getUser().getPassword())) {
//                throw new ApiErrorException(ErrorTypes.USER_PASSWORD_INCORRECT);
//            }
//            JwtToken jwtToken = tokenService.createJwtToken(userDetails);
            Map<String,Object>res=new HashMap<>();
            res.put("token",jwtToken.getRawToken().getToken());
            res.put("permission",map.getOrDefault("permission","普通管理员"));
            return res;
        } else {
            LOGGER.error("unknown login user type({})", loginRequest.getUserType());
            throw new ForbiddenException();
        }
    }

    @ApiOperation(tags = "PUBLIC", value = "用户名密码登录")
    @PostMapping(value = "/user/login-with-password",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String,Object> userLoginWithPassword(@RequestBody @NotNull @Valid PasswordLoginRequest loginRequest) {
        if (UserType.CORP == loginRequest.getUserType()) {
//            Map<String,Object> map=userManager.getAdminByUsername(loginRequest.getUsername());
            Map<String,Object> map=userManager.getUserByUsername(loginRequest.getUsername());
            if(map==null){
                map=new HashMap<>();
            }
            if (!DefaultPasswordEncoder.getDefaultInstance().isValidPassword(loginRequest.getPassword(), map.getOrDefault("password","").toString())) {
                throw new ApiErrorException(ErrorTypes.USER_PASSWORD_INCORRECT);
            }
            JwtToken jwtToken = tokenService.createJwtToken(map);

//            OkrUserDetails userDetails = (OkrUserDetails) userDetailsService.loadUserByUsername(loginRequest.getUsername());
//            if (!DefaultPasswordEncoder.getDefaultInstance().isValidPassword(loginRequest.getPassword(), userDetails.getUser().getPassword())) {
//                throw new ApiErrorException(ErrorTypes.USER_PASSWORD_INCORRECT);
//            }
//            JwtToken jwtToken = tokenService.createJwtToken(userDetails);
            Map<String,Object>res=new HashMap<>();
            res.put("token",jwtToken.getRawToken());
            res.put("permission",map.getOrDefault("permission","普通管理员"));
            return res;
        } else {
            LOGGER.error("unknown login user type({})", loginRequest.getUserType());
            throw new ForbiddenException();
        }
    }


    @ApiOperation(tags = "PUBLIC", value = "用户名密码登录")
    @PostMapping(value = "/user/register",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public RawJwtToken userRegister(@RequestBody @NotNull @Valid PasswordLoginRequest loginRequest) {
        if (UserType.CORP == loginRequest.getUserType()) {
//            Map<String,Object> map=userManager.getAdminByUsername(loginRequest.getUsername());
            Map<String,Object> map=userManager.getUserByUsername(loginRequest.getUsername());
            if(map==null){
                map=new HashMap<>();
            }
            System.out.println(map);
            if (!DefaultPasswordEncoder.getDefaultInstance().isValidPassword(loginRequest.getPassword(), map.getOrDefault("password","").toString())) {
                throw new ApiErrorException(ErrorTypes.USER_PASSWORD_INCORRECT);
            }
            JwtToken jwtToken = tokenService.createJwtToken(map);

//            OkrUserDetails userDetails = (OkrUserDetails) userDetailsService.loadUserByUsername(loginRequest.getUsername());
//            if (!DefaultPasswordEncoder.getDefaultInstance().isValidPassword(loginRequest.getPassword(), userDetails.getUser().getPassword())) {
//                throw new ApiErrorException(ErrorTypes.USER_PASSWORD_INCORRECT);
//            }
//            JwtToken jwtToken = tokenService.createJwtToken(userDetails);
            return jwtToken.getRawToken();
        } else {
            LOGGER.error("unknown login user type({})", loginRequest.getUserType());
            throw new ForbiddenException();
        }
    }



    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setUserManager(UserManager userManager){this.userManager=userManager;}

    @Autowired
    public void setTokenService(TokenServiceImpl tokenService) {
        this.tokenService = tokenService;
    }
}
