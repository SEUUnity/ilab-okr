package com.industics.ilab.okr.dal.manager;

import com.industics.ilab.okr.dal.dao.mapper.PositionMapper;
import com.industics.ilab.okr.dal.dao.mapper.UserMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class PositionManager {

    private PositionMapper positionMapper;


    public void addPosition(String position_name,String degree,String location,String position_detail,
                        String position_require,String salary,int salary_count,int quota,String bonus_type){
        positionMapper.addPosition(position_name,degree,location,position_detail,position_require,salary,salary_count,quota,bonus_type);
    }

    public void updatePosition(String position_id,String position_name,String degree,String location,String position_detail,
                               String position_require,String salary,int salary_count,int quota,String bonus_type){
        positionMapper.updatePosition(position_id,position_name,degree,location,position_detail,position_require,salary,salary_count,quota,bonus_type);
    }

    public void deletePosition(String position_id){
        positionMapper.deletePosition(position_id);
    }

    public List<Map<String,Object>> getPositions(){
        return positionMapper.getPositions();
    }
    public Map<String,Object> getPositionByID(String position_id){
        return positionMapper.getPositionByID(position_id);
    }
    public int getPositionsNumByName(String position_name){
        return positionMapper.getPositionsNumByName(position_name);
    }

    public List<Map<String,Object>> getBriefPositions(){
        return positionMapper.getBriefPositions();
    }


    @Autowired
    public void setPositionMapper(PositionMapper positionMapper){
        this.positionMapper=positionMapper;
    }
}
