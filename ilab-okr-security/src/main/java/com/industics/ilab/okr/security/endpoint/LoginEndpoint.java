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

import com.alibaba.fastjson.JSONObject;
import com.industics.ilab.okr.apiobjects.etype.ErrorTypes;
import com.industics.ilab.okr.dal.manager.UserManager;
import com.industics.ilab.okr.security.apiobjects.PasswordLoginRequest;
import com.industics.ilab.okr.security.apiobjects.UserRegister;
import com.industics.ilab.okr.security.apiobjects.UserType;
import com.industics.ilab.okr.security.token.JwtToken;
import com.industics.ilab.okr.security.token.RawJwtToken;
import com.industics.ilab.okr.security.token.TokenServiceImpl;
import com.industics.ilab.okr.security.userdetails.UserDetailsService;
import com.industics.ilab.okr.security.utils.Appdata;
import com.industics.ilab.okr.security.utils.HttpClientUtil;
import com.industics.ilab.okr.security.utils.Result;
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
import java.util.*;

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

    @ApiOperation(tags = "PUBLIC", value = "小程序端用户名密码登录")
    @PostMapping(value = "/user/login",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public RawJwtToken userLoginWithPassword(@RequestBody @NotNull @Valid PasswordLoginRequest loginRequest) {
        if (UserType.CORP == loginRequest.getUserType()) {
            Map<String,Object> map=userManager.getUserByEmail(loginRequest.getUsername());
            if(map==null){
                map=new HashMap<>();
            }
            if (!DefaultPasswordEncoder.getDefaultInstance().isValidPassword(loginRequest.getPassword(), map.getOrDefault("password","").toString())) {
                throw new ApiErrorException(ErrorTypes.USER_PASSWORD_INCORRECT);
            }
            JwtToken jwtToken = tokenService.createJwtTokenForBL(map);
            return jwtToken.getRawToken();
        } else {
            LOGGER.error("unknown login user type({})", loginRequest.getUserType());
            throw new ForbiddenException();
        }
    }


    @PostMapping("/me/login")
    public Result user_login(
            @RequestBody @NotNull @Valid Map<String,String> codeMap
    ){
        if(codeMap==null||!codeMap.containsKey("code")){
            return Result.error(33,"缺少参数");
        }

        String code=codeMap.get("code");
        Map<String, String> param = new HashMap<>();
        param.put("appid", Appdata.WX_LOGIN_APPID);
        param.put("secret", Appdata.WX_LOGIN_SECRET);
        param.put("js_code", code);
        param.put("grant_type", Appdata.WX_LOGIN_GRANT_TYPE);
        // 发送请求
        System.out.println(code);
        String wxResult = HttpClientUtil.doGet(Appdata.WX_LOGIN_URL, param);
        System.out.println(wxResult);
        JSONObject jsonObject = JSONObject.parseObject(wxResult);
        // 获取参数返回的
        String session_key = jsonObject.get("session_key").toString();
        String open_id = jsonObject.get("openid").toString();

        Map<String,Object> map=userManager.getUserByID(open_id);
        Map<String,Object> res=new HashMap<>();
        res.put("open_id",open_id);
        res.put("session_key",session_key);
        if(map==null){
            res.put("hasPermission",false);
            res.put("token","");
            return Result.ok(res);
        }

        JwtToken jwtToken = tokenService.createJwtTokenForBL(map);
        res.put("hasPermission",true);
        res.put("token",jwtToken.getRawToken().getToken());
        return Result.ok(res);

    }


    @ApiOperation(tags = "PUBLIC", value = "小程序端用户名注册")
    @PostMapping(value = "/user/register",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public Result userRegisters(@RequestBody @NotNull @Valid UserRegister user) {
        Map<String,Object> u=userManager.getUserByEmail(user.getEmail());
        if(u!=null){
            return Result.error(27,"用户已存在");
        }
        Map<String,Object> userRegister=userManager.getRegisterByEmail(user.getEmail());
        if(userRegister==null||!user.getCode().equals(userRegister.getOrDefault("code",""))){
            return Result.error(29,"验证码错误");
        }
        if(userManager.validTime(userRegister)){
            userManager.addUserBL(user.getOpen_id(),user.getWork_num(),user.getName(),
                    user.getAvatar(),user.getEmail(),user.getPhone(),user.getWe_chat());
        }else{
            return Result.error(30,"验证码超时");
        }

        return Result.ok("注册成功");
    }



    @ApiOperation(tags = "PUBLIC", value = "发邮件")
    @PostMapping(value = "/user/sendMail",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public Result userSendMail(@RequestBody @NotNull @Valid Map<String,String> email) {
        if (email == null||email.isEmpty()||!email.containsKey("email")) {
            return Result.error(26,"邮箱为空");
        }
        Map<String,Object> user=userManager.getUserByEmail(email.get("email"));
        if(user!=null){
            return Result.error(27,"用户已存在");
        }
        int res=userManager.sendMail(email.get("email"));
        if(res==2){
            return Result.error(28,"五分钟内请不要重复发送");
        }
        return Result.ok("发送成功");
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
