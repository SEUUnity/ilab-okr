package com.industics.ilab.okr.web.endpoint;


import com.industics.ilab.okr.dal.manager.ApplicantManager;
import com.industics.ilab.okr.security.utils.Result;
import com.industics.ilab.okr.security.utils.SFTP;
import com.industics.ilab.okr.web.apiobjects.UploadResume;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.*;

@Api(tags = "APPLICANT", value = "应聘者API")
@RestController
@RequestMapping(value = "/v2/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class ApplicantEndpoint {
    ApplicantManager applicantManager;
    @Autowired
    public void setApplicantManager(ApplicantManager applicantManager){
        this.applicantManager=applicantManager;
    }

    @PostMapping("/user/getApplicant")
    @ApiOperation(value = "获取应聘者流程")
    public Result getApplicant(@RequestBody @NotNull @Valid Map<String,String> user){
        if(user==null||!user.containsKey("user_id")){
            return Result.error(33,"缺少参数");
        }
        String user_id=user.get("user_id");
        List<Map<String,Object>>map=applicantManager.getApplicantByBL(user_id);
        if(map==null){
            return Result.error(22,"用户不存在");
        }
        List<Map<String,Object>>res=new ArrayList<>();
        for(int i=0;i<map.size();i++){
            Map<String,Object>m=new HashMap<>();
            m.put("id",i);
            m.put("applicant_id",map.get(i).get("applicant_id"));
            m.put("applicant_name",map.get(i).get("name"));
            m.put("position_name",map.get(i).get("position_name"));
            int pro=Integer.parseInt(map.get(i).get("process").toString());

            if(pro==0){
                m.put("current_status","process");
                m.put("status",0);
                m.put("res","");
            }else if(pro==1){
                m.put("current_status","finish");
                m.put("status",1);
                m.put("res","");
            }else if(pro==-1){
                m.put("res","不");
                m.put("current_status","error");
                m.put("status",1);
            }

            res.add(m);
        }
        return Result.ok("ok").put("data",res);
    }

    @PostMapping("/user/addApplicant")
    @ApiOperation(value = "上传简历")
    public Result addApplicant(@RequestParam String open_id,
                               @RequestParam String name,
                               @RequestParam String phone,
                               @RequestParam String position_id,
                               @RequestParam MultipartFile multipartFile,
                               @RequestParam String work_num){
        Map<String,Object> map=applicantManager.getApplicantByID(open_id);
        if(map==null){
            File file= SFTP.multipartFileToFile(multipartFile);
            String url=SFTP.uploadFile(file);
            applicantManager.addApplicant(open_id,name,phone,position_id,url,work_num);
        }else {
            return Result.error(36,"已投过简历无法再投");
        }

        return Result.ok();
    }

    @GetMapping("/user/getResume")
    @ApiOperation(value = "上传简历")
    public Result addApplicant(@RequestParam String open_id){
        Map<String,Object> map=applicantManager.getApplicantByID(open_id);
        if(map==null){
            return Result.error(22,"用户不存在");
        }

        return Result.ok().put("data",applicantManager.getResume(open_id));
    }
}
