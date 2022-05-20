package com.industics.ilab.okr.web.endpoint;

import com.industics.ilab.okr.dal.manager.PositionManager;
import com.industics.ilab.okr.dal.manager.UserManager;
import com.industics.ilab.okr.security.utils.Result;
import com.industics.ilab.okr.web.apiobjects.AddPosition;
import com.industics.ilab.okr.web.apiobjects.AdminLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Api(tags = "POSITION", value = "职位API")
@RestController
@RequestMapping(value = "/v2/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class PositionEndpoint {

    private PositionManager positionManager;

    @Autowired
    public void setPositionManager(PositionManager positionManager){this.positionManager=positionManager;}

    @PostMapping("/addPosition")
    @ApiOperation(value = "添加职位")
    public Result logins(@RequestBody @NotNull @Valid AddPosition addPosition){
        System.out.println("11111111111111");
        System.out.println(addPosition.getPosition_name()+" "+addPosition.getBonus_type());
        positionManager.addPosition(addPosition.getPosition_name(),addPosition.getBonus_type());
        return Result.ok("ok");
    }
}
