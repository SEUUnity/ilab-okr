package com.industics.ilab.okr.web.endpoint;

import com.industics.ilab.okr.dal.manager.ApprovalManager;
import com.industics.ilab.okr.dal.manager.BonusManager;
import com.industics.ilab.okr.security.SecurityContexts;
import com.industics.ilab.okr.security.token.JwtToken;
import com.industics.ilab.okr.security.utils.Result;
import com.industics.ilab.okr.web.apiobjects.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Api(tags = "APPROVAL", value = "奖金审批API")
@RestController
@RequestMapping(value = "/v2/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class ApprovalEndpoint {
    ApprovalManager approvalManager;
    BonusManager bonusManager;
    @Autowired
    public void setApprovalManager(ApprovalManager approvalManager){this.approvalManager=approvalManager;}
    @Autowired
    public void setBonusManager(BonusManager bonusManager){this.bonusManager=bonusManager;}

    @PostMapping("/updateApproval")
    @ApiOperation(value = "修改奖金")
    public Result updateApproval(@RequestBody @NotNull @Valid UpdateApproval updateApproval){
        Map<String,Object> map=approvalManager.getApprovalByID(updateApproval.getApproval_id());
        JwtToken context = SecurityContexts.getLoginUserContext();
        if(map==null){
            return Result.error(15,"奖金审批不存在");
        }
        int status=0;
        if(updateApproval.getStatus().equals("未通过")){
            status=-1;
        }else if(updateApproval.getStatus().equals("待审批")){
            status=0;
        }else if(updateApproval.getStatus().equals("已审批")){
            status=1;
        }else if(updateApproval.getStatus().equals("已发放")){
            status=2;
        }
        if(status==2){
            if(Integer.parseInt(map.get("status").toString())!=2){
                Map<String,Object> bonus = approvalManager.getInfoByApprovalId(updateApproval.getApproval_id());
                bonusManager.addGrantBonus(context.getUserId(),updateApproval.getApproval_id(),
                        bonus.get("bonus_type").toString(), Integer.parseInt(bonus.get("amount").toString()));
            }
        }
        approvalManager.updateApproval(updateApproval.getApproval_id(),status);
        return Result.ok("ok");
    }

    @PostMapping("/multiUpdateApproval")
    @ApiOperation(value = "批量修改奖金")
    public Result updateApproval(@RequestBody @NotNull @Valid MultiUpdateApproval multiUpdateApproval){
        JwtToken context = SecurityContexts.getLoginUserContext();
        for(int i=0;i<multiUpdateApproval.getApproval_ids().size();i++){
            Map<String,Object> map=approvalManager.getApprovalByID(multiUpdateApproval.getApproval_ids().get(i));
            if(map==null){
                return Result.error(15,"奖金审批不存在");
            }
            int status=0;
            if(multiUpdateApproval.getStatus().equals("未通过")){
                status=-1;
            }else if(multiUpdateApproval.getStatus().equals("待审批")){
                status=0;
            }else if(multiUpdateApproval.getStatus().equals("已审批")){
                status=1;
            }else if(multiUpdateApproval.getStatus().equals("已发放")){
                status=2;
            }
            approvalManager.updateApproval(multiUpdateApproval.getApproval_ids().get(i),status);
            if(status==2){
                if(Integer.parseInt(map.get("status").toString())!=2) {
                    Map<String,Object> bonus = approvalManager.getInfoByApprovalId(multiUpdateApproval.getApproval_ids().get(i));
                    bonusManager.addGrantBonus(context.getUserId(), multiUpdateApproval.getApproval_ids().get(i),
                            bonus.get("bonus_type").toString(), Integer.parseInt(bonus.get("amount").toString()));
                }
            }
        }
        return Result.ok("ok");
    }

    @PostMapping("/getApprovalByStatus")
    @ApiOperation(value = "按状态获得奖金审批流程")
    public Result getApprovalByStatus(@RequestBody @NotNull @Valid GetByStatus getByStatus){
        List<Integer> list=new ArrayList<>();
        for(int i = 0; i< getByStatus.getStatus().size(); i++){
            if(getByStatus.getStatus().get(i).equals("未通过")){
                list.add(-1);
            }else if(getByStatus.getStatus().get(i).equals("待审批")){
                list.add(0);
            }else if(getByStatus.getStatus().get(i).equals("已审批")){
                list.add(1);
            }else if(getByStatus.getStatus().get(i).equals("已发放")){
                list.add(2);
            }
        }

        List<Map<String,Object>> result=approvalManager.getApprovalByStatus(list,
                getByStatus.getPage_num(), getByStatus.getData_num());
        System.out.println(result);
        int num=approvalManager.getApprovalByStatusCount(list);
        for(int i=0;i<result.size();i++){
            if(result.get(i).containsKey("start_time")){
                result.get(i).put("start_time",result.get(i).get("start_time").toString()
                        .replace('T',' ').replace(".0",""));
            }
            if(result.get(i).containsKey("pass_time")){
                result.get(i).put("pass_time",result.get(i).get("pass_time").toString()
                        .replace('T',' ').replace(".0",""));
            }
            if(result.get(i).containsKey("update_time")){
                result.get(i).put("update_time",result.get(i).get("update_time").toString()
                        .replace('T',' ').replace(".0",""));
            }
            if(result.get(i).containsKey("over_time")){
                result.get(i).put("over_time",result.get(i).get("over_time").toString()
                        .replace('T',' ').replace(".0",""));
            }
            switch (Integer.parseInt(result.get(i).get("status").toString())){
                case -1:
                    result.get(i).put("status","未通过");
                    break;
                case 0:
                    result.get(i).put("status","待审批");
                    break;
                case 1:
                    result.get(i).put("status","已审批");
                    break;
                case 2:
                    result.get(i).put("status","已发放");
                    break;
                default:
                    break;
            }

        }
        return Result.ok("ok").put("count",num).put("data",result);
    }

}
