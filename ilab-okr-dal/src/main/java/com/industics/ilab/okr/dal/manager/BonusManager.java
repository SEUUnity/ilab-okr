package com.industics.ilab.okr.dal.manager;

import com.industics.ilab.okr.dal.dao.mapper.BonusMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class BonusManager {
    private BonusMapper bonusMapper;
    @Autowired
    public void setBonusMapper(BonusMapper bonusMapper){this.bonusMapper=bonusMapper;}

    public void addBonus(String bonus_type,int amount){
        bonusMapper.addBonus(bonus_type,amount);
    }
    public void addGrantBonus(@Param("admin_id") String admin_id,
                              @Param("approval_id") String approval_id,
                              @Param("amount") int amount){
        bonusMapper.addGrantBonus(admin_id,approval_id,amount);
    }

    public void updateBonus(String bonus_type,int amount){
        bonusMapper.updateBonus(bonus_type,amount);
    }

    public void deleteBonus(String position_id){
        bonusMapper.deleteBonus(position_id);
    }

    public List<Map<String,Object>> getBonus(){
        return bonusMapper.getBonus();
    }
    public List<String> getBonusType(){
        return bonusMapper.getBonusType();
    }
    public Map<String,Object> getBonusByID(String Bonus_type){
        return bonusMapper.getBonusByID(Bonus_type);
    }

}
