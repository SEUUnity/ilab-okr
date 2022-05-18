package com.industics.ilab.okr.security.controller;

import com.industics.ilab.okr.dal.dao.mapper.UserMapper;
import com.industics.ilab.okr.security.utils.Result;
import com.industics.ilab.okr.security.utils.TokenUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
@RestController
public class UserLogin {
    UserMapper userMapper;

    @ApiOperation(tags = "PUBLIC", value = "用户名密码登录")
    @GetMapping ("/admin/position")
    public Result login(){//@RequestParam("username") String username,
                        //@RequestParam("password") String password){
        //int admin=userMapper.adminLogin(username,password);
//        int admin=1;
//        if(admin==0){
//                Result result=Result.error(44,"登陆失败");
//                return result;
//        }
//        String token=TokenUtils.generateToken(username,password,1);
        //Result result=Result.ok("访问成功").put("token","sss").put("identity",1);
        Result result=Result.ok("访问成功").put("position_id","10001").put("position_name","软件部")
                .put("bonus_type","A1").put("update_time","2022-02-12");
        return result;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
}
