package com.industics.ilab.okr.web.endpoint;

import com.industics.ilab.okr.dal.manager.ApprovalManager;
import com.industics.ilab.okr.security.utils.Result;
import com.industics.ilab.okr.web.apiobjects.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.standard.expression.Each;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Api(tags = "APPROVAL", value = "奖金审批API")
@RestController
@RequestMapping(value = "/v2/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class ApprovalEndpoint {
    ApprovalManager approvalManager;
    @Autowired
    public void setApprovalManager(ApprovalManager approvalManager){this.approvalManager=approvalManager;}

    @PostMapping("/updateApproval")
    @ApiOperation(value = "修改奖金")
    public Result updateApproval(@RequestBody @NotNull @Valid UpdateApproval updateApproval){
        Map<String,Object> map=approvalManager.getApprovalByID(updateApproval.getApproval_id());
        if(map==null){
            return Result.error(15,"奖金审批不存在");
        }
        approvalManager.updateApproval(updateApproval.getApproval_id(),updateApproval.getStatus());
        return Result.ok("ok");
    }

    @PostMapping("/multiUpdateApproval")
    @ApiOperation(value = "修改奖金")
    public Result updateApproval(@RequestBody @NotNull @Valid MultiUpdateApproval multiUpdateApproval){
        for(int i=0;i<multiUpdateApproval.getApproval_ids().size();i++){
            Map<String,Object> map=approvalManager.getApprovalByID(multiUpdateApproval.getApproval_ids().get(i));
            if(map==null){
                return Result.error(15,"奖金审批不存在");
            }
            approvalManager.updateApproval(multiUpdateApproval.getApproval_ids().get(i),multiUpdateApproval.getStatus());
        }
        return Result.ok("ok");
    }

    @PostMapping("/getApprovalByStatus")
    @ApiOperation(value = "修改奖金")
    public Result getApprovalByStatus(@RequestBody @NotNull @Valid GetApprovalByStatus getApprovalByStatus){
        List<Map<String,Object>> result=approvalManager.getApprovalByStatus(getApprovalByStatus.getStatus(),
                getApprovalByStatus.getPage_num(),getApprovalByStatus.getData_num());
        int num=approvalManager.getApprovalByStatusCount(getApprovalByStatus.getStatus());
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
                    result.get(i).put("status","不通过");
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

    @PostMapping("/getApproval")
    @ApiOperation(value = "修改奖金")
    public Result getApproval(@RequestBody @NotNull @Valid EachPage eachPage){

        List<Map<String,Object>> result=approvalManager.getApproval(eachPage.getPage_num(),eachPage.getData_num());
        int num=approvalManager.getApprovalCount();
        for(int i=0;i<result.size();i++){
            if(result.get(i).containsKey("update_time")){
                result.get(i).put("update_time",result.get(i).get("update_time").toString()
                        .replace('T',' ').replace(".0",""));
            }
            switch (Integer.parseInt(result.get(i).get("status").toString())){
                case -1:
                    result.get(i).put("status","不通过");
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
        return Result.ok("ok").put("data",result).put("count",num);
    }
}
