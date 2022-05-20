package com.industics.ilab.okr.dal.manager;

import com.industics.ilab.okr.dal.dao.mapper.BonusMapper;
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

    public void updateBonus(String bonus_type,int amount){
        bonusMapper.updateBonus(bonus_type,amount);
    }

    public void deleteBonus(String position_id){
        bonusMapper.deleteBonus(position_id);
    }

    public List<Map<String,Object>> getBonus(){
        return bonusMapper.getBonus();
    }
    public List<Map<String,Object>> getBonusType(){
        return bonusMapper.getBonusType();
    }
    public Map<String,Object> getBonusByID(String Bonus_type){
        return bonusMapper.getBonusByID(Bonus_type);
    }

}
