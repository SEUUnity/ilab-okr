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


import com.atlassian.security.password.DefaultPasswordEncoder;
import com.industics.ilab.okr.apiobjects.user.GroupVO;
import com.industics.ilab.okr.apiobjects.user.UserTreeVO;
import com.industics.ilab.okr.apiobjects.user.UserVO;
import com.industics.ilab.okr.dal.manager.UserManager;
import com.industics.ilab.okr.dal.service.GroupService;
import com.industics.ilab.okr.dal.service.UserGroupService;
import com.industics.ilab.okr.security.SecurityContexts;
import com.industics.ilab.okr.security.apiobjects.UserType;
import com.industics.ilab.okr.security.exception.TokenInvalidException;
import com.industics.ilab.okr.security.token.JwtToken;

import com.industics.ilab.okr.security.utils.Result;
import com.industics.ilab.okr.security.utils.TokenUtils;
import com.industics.ilab.okr.web.apiobjects.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Api(tags = "USER", value = "用户API")
@RestController
@RequestMapping(value = "/v2/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserEndpoint extends AbstractEndpoint {

    private UserManager userManager;
    private UserGroupService userGroupService;
    private GroupService groupService;

    @Autowired
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    @Autowired
    public void setUserGroupService(UserGroupService userGroupService) {
        this.userGroupService = userGroupService;
    }

    @Autowired
    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping(value = "/user-info")
    @ApiOperation(value = "从Token中获取用户信息")
    public UserVO getUserInfo() {
        JwtToken context = SecurityContexts.getLoginUserContext();
        if (UserType.CORP == context.getUserType()) {
            System.out.println(context.getUserId());
            return userManager.getUserVO("8730a2feab4c11e88828525400857709");
        } else {
            throw new TokenInvalidException(context.getRawToken().getToken());
        }
    }

    @GetMapping(value = "/user-tree")
    @ApiOperation(value = "获取用户okr管理树")
    public UserTreeVO getUserTree() {
        return userManager.getUserTree();
    }

    @GetMapping(value = "/user-groups")
    @ApiOperation(value = "从Token中获取用户的所有组")
    public List<String> getUserGroups() {
        JwtToken context = SecurityContexts.getLoginUserContext();
        if (UserType.CORP == context.getUserType()) {
            return userGroupService.getUserGroupsByUserId(context.getUserId());
        } else {
            throw new TokenInvalidException(context.getRawToken().getToken());
        }
    }

    @GetMapping(value = "/groups")
    @ApiOperation(value = "获取用户组列表")
    public List<GroupVO> getGroupList(@RequestParam(value = "type", required = false) String type) {
        JwtToken context = SecurityContexts.getLoginUserContext();
        if (UserType.CORP == context.getUserType()) {
            return groupService.getGroupList(type);
        } else {
            throw new TokenInvalidException(context.getRawToken().getToken());
        }
    }

    @GetMapping(value = "/group-users/{group_id}")
    @ApiOperation(value = "获取用户组的人员")
    public List<UserVO> getUserList(@PathVariable("group_id") String groupId) {
        JwtToken context = SecurityContexts.getLoginUserContext();
        if (UserType.CORP == context.getUserType()) {
            return groupService.getUsersByGroup(groupId);
        } else {
            throw new TokenInvalidException(context.getRawToken().getToken());
        }
    }

    @GetMapping(value = "/group-users/type/{group_type}")
    @ApiOperation(value = "获取某类用户组的人员")
    public List<UserVO> getUserListByGroupType(@PathVariable("group_type") String groupType) {
        JwtToken context = SecurityContexts.getLoginUserContext();
        if (UserType.CORP == context.getUserType()) {
            return groupService.getUsersByGroupType(groupType);
        } else {
            throw new TokenInvalidException(context.getRawToken().getToken());
        }
    }

    @GetMapping(value = "/users")
    @ApiOperation(value = "获取用户")
    public List<UserVO> getUsersPage(@RequestParam(value = "fullname", required = false) String fullname,
                                     @RequestParam(value = "mobile", required = false) String mobile) {
        JwtToken context = SecurityContexts.getLoginUserContext();
        if (UserType.CORP == context.getUserType()) {
            return userManager.getUsers(fullname, mobile);
        } else {
            throw new TokenInvalidException(context.getRawToken().getToken());
        }
    }

    @PostMapping("/admin/login")
    @ApiOperation(value = "登录")
    public Result logins(@RequestBody @NotNull @Valid AdminLogin adminLogin){

//        JwtToken context = SecurityContexts.getLoginUserContext();
//        if (UserType.CORP == context.getUserType()) {
//            return userManager.getUsers(username, password);
//        } else {
//            throw new TokenInvalidException(context.getRawToken().getToken());
//        }
        int admin=userManager.adminLogin(adminLogin.getUsername(),adminLogin.getPassword());
        //int admin=1;
        if(admin==0){
            Result result=Result.error(44,"登陆失败");
            return result;
        }
        //String token= TokenUtils.generateToken(username,password,1);
        Result result=Result.ok("访问成功").put("token","sss").put("identity",1);
        return result;
    }


    @PostMapping("/addAdmin")
    @ApiOperation(value = "添加管理员")
    public Result addAdmin(@RequestBody @NotNull @Valid AddAdmin addAdmin){
        Map<String,Object> m=userManager.getAdminByUsername(addAdmin.getUsername());
        if(m!=null){
            return Result.error(17,"用户已存在");
        }
        if(!(addAdmin.getPermission().equals("普通管理员")||
                addAdmin.getPermission().equals("超级管理员"))){
            return Result.error(20,"权限格式错误");
        }
        String encodePassword = DefaultPasswordEncoder.getDefaultInstance().encodePassword(addAdmin.getPassword());
        userManager.addAdmin(addAdmin.getName(),addAdmin.getUsername(),encodePassword,addAdmin.getPermission());
        return Result.ok("ok");
    }

    @DeleteMapping("/deleteAdmin")
    @ApiOperation(value = "添加管理员")
    public Result addAdmin(@RequestBody @NotNull @Valid Map<String,String> data){
        Map<String,Object> m=userManager.getAdminByID(data.getOrDefault("admin_id",""));
        if(m==null){
            return Result.error(18,"用户不存在");
        }
        userManager.deleteAdmin(data.get("admin_id"));
        return Result.ok("ok");
    }

    @DeleteMapping("/multiDeleteAdmin")
    @ApiOperation(value = "添加管理员")
    public Result multiDeleteAdmin(@RequestBody @NotNull @Valid List<Map<String,String>> data){

        for(int i=0;i<data.size();i++){
            Map<String,Object> map=userManager.getAdminByID(data.get(i).getOrDefault("admin_id",""));
            if(map==null){
                return Result.error(13,"奖金类型不存在");
            }
            userManager.deleteAdmin(data.get(i).get("admin_id"));
        }
        return Result.ok("ok");
    }

    @PostMapping("/updateAdmin")
    @ApiOperation(value = "添加管理员")
    public Result updateAdmin(@RequestBody @NotNull @Valid UpdateAdmin updateAdmin){
        Map<String,Object> m=userManager.getAdminByUsername(updateAdmin.getUsername());
        if(m!=null&&!updateAdmin.getAdmin_id().equals(m.get("admin_id").toString())){
            return Result.error(17,"用户已存在");
        }
        if(!(updateAdmin.getPermission().equals("普通管理员")||
                updateAdmin.getPermission().equals("超级管理员"))){
            return Result.error(20,"权限格式错误");
        }
        String encodePassword = DefaultPasswordEncoder.getDefaultInstance().encodePassword(updateAdmin.getPassword());
        userManager.updateAdmin(updateAdmin.getAdmin_id(),updateAdmin.getName(),updateAdmin.getUsername(),encodePassword,updateAdmin.getPermission());
        return Result.ok("ok");
    }

    @GetMapping("/getAdmins")
    @ApiOperation(value = "添加管理员")
    public Result getAdmins(){
        List<Map<String,Object>>result=userManager.getAdmins();
        for(int i=0;i<result.size();i++){
            result.get(i).put("update_time",result.get(i).get("update_time").toString()
                    .replace('T',' ').replace(".0",""));
            result.get(i).put("create_time",result.get(i).get("create_time").toString()
                    .replace('T',' ').replace(".0",""));
            result.get(i).put("last_login",result.get(i).get("last_login").toString()
                    .replace('T',' ').replace(".0",""));
        }
        return Result.ok("ok").put("data",result);
    }

    @PostMapping("/getUsers")
    @ApiOperation(value = "获取用户")
    public Result getUsers(@RequestBody @NotNull @Valid GetByStatus getByStatus){
        List<Map<String,Object>>result=userManager.getUsers(getByStatus.getStatus(),getByStatus.getPage_num(),getByStatus.getData_num());
        for(int i=0;i<result.size();i++){
            result.get(i).put("create_time",result.get(i).get("create_time").toString()
                    .replace('T',' ').replace(".0",""));
            result.get(i).put("last_login",result.get(i).get("last_login").toString()
                    .replace('T',' ').replace(".0",""));
            result.get(i).put("update_time",result.get(i).get("update_time").toString()
                    .replace('T',' ').replace(".0",""));
        }
        return Result.ok("ok").put("data",result);
    }

    @PostMapping("/updateUserStatus")
    @ApiOperation(value = "获取用户")
    public Result getUsers(@RequestBody @NotNull @Valid UpdateStatus updateStatus){
        if(!(updateStatus.getStatus().equals("已激活")||updateStatus.getStatus().equals("未激活"))){
            return Result.error(21,"状态格式错误");
        }
        userManager.updateUserStatus(updateStatus.getIds(),updateStatus.getStatus());
        return Result.ok("ok");
    }
}
