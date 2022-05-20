package com.industics.ilab.okr.dal.manager;

import com.industics.ilab.okr.dal.dao.mapper.PositionMapper;
import com.industics.ilab.okr.dal.dao.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PositionManager {

    private PositionMapper positionMapper;


    public void addPosition(String position_name,String bonus_type){
        System.out.println("22222222222");
        System.out.println(position_name+" "+bonus_type);
        positionMapper.addPosition(position_name,bonus_type);
    }

    @Autowired
    public void setPositionMapper(PositionMapper positionMapper){
        this.positionMapper=positionMapper;
    }
}
