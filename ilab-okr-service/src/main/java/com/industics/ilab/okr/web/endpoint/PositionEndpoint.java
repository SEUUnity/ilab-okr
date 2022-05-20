package com.industics.ilab.okr.web.endpoint;

import com.industics.ilab.okr.dal.manager.PositionManager;
import com.industics.ilab.okr.dal.manager.UserManager;
import com.industics.ilab.okr.security.utils.Result;
import com.industics.ilab.okr.web.apiobjects.AddPosition;
import com.industics.ilab.okr.web.apiobjects.AdminLogin;
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
            return Result.error(11,"重复添加");
        }
        positionManager.addPosition(addPosition.getPosition_name(),addPosition.getBonus_type());
        return Result.ok("ok");
    }

    @PostMapping("/updatePosition")
    @ApiOperation(value = "修改职位")
    public Result updatePosition(@RequestBody @NotNull @Valid UpdatePosition updatePosition){
        positionManager.updatePosition(updatePosition.getPosition_id(),updatePosition.getPosition_name(),updatePosition.getBonus_type());
        return Result.ok("ok");
    }

    @PostMapping("/deletePosition")
    @ApiOperation(value = "删除职位")
    public Result deletePosition(@RequestBody @NotNull @Valid Map<String,Object> position_id){
        positionManager.deletePosition(position_id.get("position_id").toString());
        return Result.ok("ok");
    }

    @PostMapping("/multiDeletePosition")
    @ApiOperation(value = "删除职位")
    public Result multiDeletePosition(@RequestBody @NotNull @Valid List<Map<String,Object>> position_id){
        for(int i=0;i<position_id.size();i++){
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
}
