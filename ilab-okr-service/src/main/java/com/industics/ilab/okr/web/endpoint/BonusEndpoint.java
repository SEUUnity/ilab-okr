package com.industics.ilab.okr.web.endpoint;

import com.industics.ilab.okr.dal.manager.BonusManager;
import com.industics.ilab.okr.security.utils.Result;
import com.industics.ilab.okr.web.apiobjects.AddBonus;
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

@Api(tags = "BONUS", value = "奖金API")
@RestController
@RequestMapping(value = "/v2/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class BonusEndpoint {
    private BonusManager bonusManager;
    @Autowired
    public void setBonusManager(BonusManager bonusManager){this.bonusManager=bonusManager;}

    @PostMapping("/addBonus")
    @ApiOperation(value = "添加奖金")
    public Result addBonus(@RequestBody @NotNull @Valid AddBonus addBonus){
        Map<String,Object> map=bonusManager.getBonusByID(addBonus.getBonus_type());
        if(map!=null){
            return Result.error(13,"奖金类型重复添加");
        }
        bonusManager.addBonus(addBonus.getBonus_type(),addBonus.getAmount());
        return Result.ok("ok");
    }

    @PostMapping("/updateBonus")
    @ApiOperation(value = "修改奖金")
    public Result updateBonus(@RequestBody @NotNull @Valid AddBonus addBonus){
        Map<String,Object> map=bonusManager.getBonusByID(addBonus.getBonus_type());
        if(map==null){
            return Result.error(13,"奖金类型不存在");
        }
        bonusManager.updateBonus(addBonus.getBonus_type(),addBonus.getAmount());
        return Result.ok("ok");
    }

    @DeleteMapping("/deleteBonus")
    @ApiOperation(value = "删除奖金")
    public Result deleteBonus(@RequestBody @NotNull @Valid Map<String,Object> bonus_type){
        Map<String,Object> map=bonusManager.getBonusByID(bonus_type.get("bonus_type").toString());
        if(map==null){
            return Result.error(13,"奖金类型不存在");
        }
        bonusManager.deleteBonus(bonus_type.get("bonus_type").toString());
        return Result.ok("ok");
    }

    @DeleteMapping("/multiDeleteBonus")
    @ApiOperation(value = "删除奖金")
    public Result multiDeleteBonus(@RequestBody @NotNull @Valid List<Map<String,Object>> bonus_type){
        for(int i=0;i<bonus_type.size();i++){
            Map<String,Object> map=bonusManager.getBonusByID(bonus_type.get(i).get("bonus_type").toString());
            if(map==null){
                return Result.error(13,"奖金类型不存在");
            }
            bonusManager.deleteBonus(bonus_type.get(i).get("bonus_type").toString());
        }
        return Result.ok("ok");
    }

    @GetMapping("/getBonus")
    @ApiOperation(value = "获得奖金")
    public Result getBonus(){
        List<Map<String,Object>>result=bonusManager.getBonus();
        for(int i=0;i<result.size();i++){
            result.get(i).put("update_time",result.get(i).get("update_time").toString()
                    .replace('T',' ').replace(".0",""));
        }
        return Result.ok("ok").put("data",result);
    }

    @GetMapping("/getBonusType")
    @ApiOperation(value = "获得奖金")
    public Result getBonusType(){
        List<Map<String,Object>>result=bonusManager.getBonusType();
        for(int i=0;i<result.size();i++){
            result.get(i).put("update_time",result.get(i).get("update_time").toString()
                    .replace('T',' ').replace(".0",""));
        }
        return Result.ok("ok").put("data",result);
    }
}
