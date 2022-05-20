package com.industics.ilab.okr.dal.dao.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface PositionMapper {

    void addPosition(@Param("position_name") String position_name,
                   @Param("bonus_type") String bonus_type);

    void deletePosition(@Param("position_id") String position_id);

    void updatePosition(@Param("position_id") String position_id,
                     @Param("position_name") String position_name,
                     @Param("bonus_type") String bonus_type);

    List<Map<String,Object>> getPositions();
    int getPositionsNumByName(@Param("position_name") String position_name);
}
