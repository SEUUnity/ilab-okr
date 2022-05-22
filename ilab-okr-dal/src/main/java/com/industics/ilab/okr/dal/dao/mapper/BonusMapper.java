package com.industics.ilab.okr.dal.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface BonusMapper {
    void addBonus(@Param("bonus_type") String bonus_type,
                     @Param("amount") int amount);

    void deleteBonus(@Param("bonus_type") String bonus_type);

    void updateBonus(@Param("bonus_type") String bonus_type,
                        @Param("amount") int amount);

    List<Map<String,Object>> getBonus();
    List<String> getBonusType();

    Map<String,Object> getBonusByID(@Param("bonus_type") String bonus_type);

    //void startApproval()
}
