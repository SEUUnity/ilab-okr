package com.industics.ilab.okr.web.endpoint;


import com.industics.ilab.okr.dal.manager.ApplicantManager;
import com.industics.ilab.okr.dal.manager.ApprovalManager;
import com.industics.ilab.okr.dal.manager.PositionManager;
import com.industics.ilab.okr.dal.manager.UserManager;
import com.industics.ilab.okr.security.utils.Result;
import com.industics.ilab.okr.security.utils.SFTP;
import com.industics.ilab.okr.web.apiobjects.GetByStatus;
import com.industics.ilab.okr.web.apiobjects.UpdateStatus;
import com.industics.ilab.okr.web.apiobjects.UploadResume;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
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
    ApprovalManager approvalManager;
    UserManager userManager;
    private PositionManager positionManager;

    @Autowired
    public void setPositionManager(PositionManager positionManager){this.positionManager=positionManager;}
    @Autowired
    public void setApplicantManager(ApplicantManager applicantManager){
        this.applicantManager=applicantManager;
    }
    @Autowired
    public void setApprovalManager(ApprovalManager approvalManager){
        this.approvalManager=approvalManager;
    }
    @Autowired
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
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

    @PostMapping("/public/getMyApplicant")
    @ApiOperation(value = "获取应聘者流程")
    public Result getMyApplicant(@RequestBody @NotNull @Valid Map<String,String> user){
        if(user==null||!user.containsKey("applicant_id")){
            return Result.error(33,"缺少参数");
        }
        String applicant_id=user.get("applicant_id");
        Map<String,Object>map=applicantManager.getApplicantByID(applicant_id);
        if(map==null){
            return Result.error(22,"该用户未上传过简历");
        }
        Map<String,Object>m=new HashMap<>();
        m.put("id",0);
        m.put("applicant_id",map.get("applicant_id"));
        m.put("applicant_name",map.get("name"));
        m.put("position_name",map.get("position_name"));
        int pro=Integer.parseInt(map.get("process").toString());

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

        return Result.ok("ok").put("data",m);
    }

    @PostMapping("/user/addApplicant")
    @ApiOperation(value = "上传简历")
    public int addApplicant(@RequestParam String open_id,
                               @RequestParam String name,
                               @RequestParam String phone,
                               @RequestParam String position_id,
                               @RequestParam MultipartFile multipartFile,
                               @RequestParam String work_num){
        Map<String,Object> map=applicantManager.getApplicantByID(open_id);
        if(map==null){
            Map<String,Object>user=userManager.getUserByWorkNum(work_num);
            if(user==null){
                return 38;
            }
            Map<String,Object>position=positionManager.getPositionByID(position_id);
            if(Integer.parseInt(position.get("quota").toString())<=0){
                return 39;
            }
            File file= SFTP.multipartFileToFile(multipartFile);
            String url=SFTP.uploadFile(file);
            applicantManager.addApplicant(open_id,name,phone,position_id,url,work_num);
        }else {
            return 36;
        }

        return 200;
    }

    @GetMapping("/user/getResume")
    @ApiOperation(value = "获取简历")
    public Result getResume(@RequestParam String open_id){
        Map<String,Object> map=applicantManager.getApplicantByID(open_id);
        if(map==null){
            return Result.error(22,"用户不存在");
        }
        String url=applicantManager.getResume(open_id).get("resume").toString();
        String[] prefix=url.split("\\.");
        Map<String,Object>res=new HashMap<>();
        res.put("resume",url);
        res.put("prefix",prefix[prefix.length-1]);
        return Result.ok().put("data",res);
    }

    @PostMapping("/getApplicants")
    @ApiOperation(value = "获取所有用户")
    public Result getApplicants(@RequestBody @NotNull @Valid GetByStatus getByStatus){
        List<Integer>status=new ArrayList<>();
        for(int i=0;i<getByStatus.getStatus().size();i++){
            if(getByStatus.getStatus().get(i).equals("面试中")){
                status.add(0);
            }else if(getByStatus.getStatus().get(i).equals("已通过")){
                status.add(1);
            }else if(getByStatus.getStatus().get(i).equals("未通过")){
                status.add(-1);
            }else {
                return Result.error(21,"状态格式错误");
            }
        }

        List<Map<String,Object>>result=applicantManager.getApplicants(status,getByStatus.getPage_num(),getByStatus.getData_num());
        for(int i=0;i<result.size();i++){
            result.get(i).put("create_time",result.get(i).get("create_time").toString()
                    .replace('T',' ').replace(".0",""));
        }
        int num=applicantManager.getApplicantsCount(status);
        for(int i=0;i<result.size();i++){
            int process=Integer.parseInt(result.get(i).getOrDefault("process","0").toString());
            switch (process){
                case -1:
                    result.get(i).put("status","未通过");
                    break;
                case 0:
                    result.get(i).put("status","面试中");

                    break;
                case 1:
                    result.get(i).put("status","已通过");
                    break;
                default:
                    break;
            }

        }
        return Result.ok("ok").put("data",result).put("count",num);
    }

    @PostMapping("/updateApplicantStatus")
    @ApiOperation(value = "更新应聘者激活状态")
    public Result updateApplicantStatus(@RequestBody @NotNull @Valid UpdateStatus updateStatus){
        if(!(updateStatus.getStatus().equals("面试中")||updateStatus.getStatus().equals("已通过")||
                updateStatus.getStatus().equals("未通过"))){
            return Result.error(21,"状态格式错误");
        }
        int status=0;
        if(updateStatus.getStatus().equals("面试中")){
            status=0;
        }else if(updateStatus.getStatus().equals("未通过")){
            status=-1;
        }else if(updateStatus.getStatus().equals("已通过")){
            status=1;
        }
        applicantManager.updateApplicantStatus(updateStatus.getIds(),status);
        if(status==1){
            for (int i=0;i<updateStatus.getIds().size();i++){
                Map<String,Object>map=approvalManager.getApprovalByApplicant(updateStatus.getIds().get(i));
                if(map!=null){
                    continue;
                }
                Map<String,Object>applicant=applicantManager.getApplicantByID(updateStatus.getIds().get(i));
                if(applicant==null){
                    return Result.error(37,"应聘者id不存在");
                }
                Map<String,Object>user=userManager.getUserByWorkNum(applicant.get("work_num").toString());
                if(user==null){
                    return Result.error(38,"该工号不存在");
                }
                approvalManager.addApproval(user.get("user_id").toString(),updateStatus.getIds().get(i));
                Map<String,Object>position=positionManager.getPositionByID(applicant.get("position_id").toString());
                if(Integer.parseInt(position.get("quota").toString())<=0){
                    return Result.error(39,"该岗位已无名额");
                }
                positionManager.updatePosition(position.get("position_id").toString(),position.get("position_name").toString(),
                        position.get("degree").toString(),position.get("location").toString(),
                        position.get("position_detail").toString(),position.get("position_require").toString(),
                        position.get("salary").toString(),Integer.parseInt(position.get("salary_count").toString()),
                        Integer.parseInt(position.get("quota").toString())-1,position.get("bonus_type").toString());
            }

        }
        return Result.ok("ok");
    }


}
