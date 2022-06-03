package com.industics.ilab.okr.dal.manager;

import com.industics.ilab.okr.dal.dao.mapper.ApplicantMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ApplicantManager {
    private ApplicantMapper applicantMapper;
    @Autowired
    public void setApplicantMapper(ApplicantMapper applicantMapper){
        this.applicantMapper=applicantMapper;
    }

    public Map<String,Object>getApplicantByID(String applicant_id){
        return applicantMapper.getApplicantByID(applicant_id);
    }

    public List<Map<String,Object>>getApplicantByBL(String user_id){
        return applicantMapper.getApplicantByBL(user_id);
    }
    public void addApplicant(String user_id,String name,String phone,String position_id,String resume,String work_num){
        applicantMapper.addApplicant(user_id,name,phone,position_id,resume,work_num);
    }
    
}
