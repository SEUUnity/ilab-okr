package com.industics.ilab.okr.dal.dao.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface PositionMapper {

    void addPosition(@Param("position_name") String position_name,
                   @Param("bonus_type") String bonus_type);
}
