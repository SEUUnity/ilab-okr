package com.industics.ilab.okr.dal.manager;

import com.industics.ilab.okr.dal.dao.mapper.ApplicantMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApplicantManager {
    private ApplicantMapper applicantMapper;
    @Autowired
    public void setApplicantMapper(ApplicantMapper applicantMapper){
        this.applicantMapper=applicantMapper;
    }
    
}
