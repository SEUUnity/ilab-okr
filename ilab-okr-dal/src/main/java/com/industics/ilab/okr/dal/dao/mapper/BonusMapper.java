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
    void addGrantBonus(@Param("admin_id") String admin_id,
                       @Param("approval_id") String approval_id,
                       @Param("bonus_type") String bonus_type,
                       @Param("amount") int amount    );
    void deleteBonus(@Param("bonus_type") String bonus_type);

    void updateBonus(@Param("bonus_type") String bonus_type,
                        @Param("amount") int amount);

    List<Map<String,Object>> getBonus();
    List<Map<String,Object>> getAmountInfo();
    List<Map<String,Object>> getBonusAmountByMonth(@Param("start") String start,
                                                   @Param("end") String end);

    List<Map<String,Object>> getBonusTypeNum(@Param("start") String start,
                                             @Param("end") String end);
    List<String> getBonusType();

    Map<String,Object> getBonusByID(@Param("bonus_type") String bonus_type);



    //void startApproval()
}
