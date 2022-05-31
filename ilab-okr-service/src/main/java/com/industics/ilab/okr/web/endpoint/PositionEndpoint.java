package com.industics.ilab.okr.web.endpoint;

import com.industics.ilab.okr.dal.manager.PositionManager;
import com.industics.ilab.okr.security.utils.Result;
import com.industics.ilab.okr.web.apiobjects.AddPosition;
import com.industics.ilab.okr.web.apiobjects.UpdatePosition;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Api(tags = "POSITION", value = "职位API")
@RestController
@RequestMapping(value = "/v2/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class PositionEndpoint {

    private PositionManager positionManager;

    @Autowired
    public void setPositionManager(PositionManager positionManager){this.positionManager=positionManager;}

    @PostMapping("/addPosition")
    @ApiOperation(value = "添加职位")
    public Result addPosition(@RequestBody @NotNull @Valid AddPosition addPosition){
        int num=positionManager.getPositionsNumByName(addPosition.getPosition_name());
        if(num>0){
            return Result.error(11,"部门重复添加");
        }
        positionManager.addPosition(addPosition.getPosition_name(),addPosition.getDegree(),addPosition.getLocation(),
                addPosition.getPosition_detail(),addPosition.getPosition_require(),addPosition.getSalary(),
                addPosition.getSalary_count(),addPosition.getQuota(),addPosition.getBonus_type());
        return Result.ok("ok");
    }

    @PostMapping("/updatePosition")
    @ApiOperation(value = "修改职位")
    public Result updatePosition(@RequestBody @NotNull @Valid UpdatePosition updatePosition){
        Map<String,Object> map=positionManager.getPositionByID(updatePosition.getPosition_id());
        if(map==null){
            return Result.error(12,"部门不存在");
        }
        int num=positionManager.getPositionsNumByName(updatePosition.getPosition_name());
        if(num>0&&!map.get("position_name").toString().equals(updatePosition.getPosition_name())){
            return Result.error(11,"部门重复添加");
        }
        positionManager.updatePosition(updatePosition.getPosition_id(),updatePosition.getPosition_name(),
                updatePosition.getDegree(),updatePosition.getLocation(),
                updatePosition.getPosition_detail(),updatePosition.getPosition_require(),updatePosition.getSalary(),
                updatePosition.getSalary_count(),updatePosition.getQuota(),updatePosition.getBonus_type());
        return Result.ok("ok");
    }

    @DeleteMapping("/deletePosition")
    @ApiOperation(value = "删除职位")
    public Result deletePosition(@RequestBody @NotNull @Valid Map<String,Object> position_id){
        Map<String,Object> map=positionManager.getPositionByID(position_id.get("position_id").toString());
        if(map==null){
            return Result.error(12,"部门不存在");
        }
        positionManager.deletePosition(position_id.get("position_id").toString());
        return Result.ok("ok");
    }

    @DeleteMapping("/multiDeletePosition")
    @ApiOperation(value = "批量删除职位")
    public Result multiDeletePosition(@RequestBody @NotNull @Valid List<Map<String,Object>> position_id){
        for(int i=0;i<position_id.size();i++){
            Map<String,Object> map=positionManager.getPositionByID(position_id.get(i).get("position_id").toString());
            if(map==null){
                return Result.error(12,"部门不存在");
            }
            positionManager.deletePosition(position_id.get(i).get("position_id").toString());
        }
        return Result.ok("ok");
    }

    @GetMapping("/getPositions")
    @ApiOperation(value = "获得职位")
    public Result getPositions(){
        List<Map<String,Object>>result=positionManager.getPositions();
        for(int i=0;i<result.size();i++){
            result.get(i).put("update_time",result.get(i).get("update_time").toString()
                    .replace('T',' ').replace(".0",""));
        }
        return Result.ok("ok").put("data",result);
    }

    @GetMapping("/public/getBriefPositions")
    @ApiOperation(value = "获得简短职位")
    public Result getBriefPositions(){
        List<Map<String,Object>>result=positionManager.getBriefPositions();
        return Result.ok("ok").put("data",result);
    }

    @GetMapping("/public/getPosition/{position_id}")
    @ApiOperation(value = "按照id获得职位")
    public Result getPosition(@PathVariable("position_id") String position_id){
        Map<String,Object>result=positionManager.getPositionByID(position_id);
        if(result.containsKey("update_time")){
            result.put("update_time",result.get("update_time").toString()
                    .replace('T',' ').replace(".0",""));
        }
        return Result.ok("ok").put("data",result);
    }
}
