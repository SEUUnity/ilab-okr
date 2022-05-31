package com.industics.ilab.okr.web.endpoint;


import com.industics.ilab.okr.dal.manager.ApplicantManager;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "APPROVAL", value = "奖金审批API")
@RestController
@RequestMapping(value = "/v2/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class ApplicantEndpoint {
    ApplicantManager applicantManager;
    @Autowired
    public void setApplicantManager(ApplicantManager applicantManager){
        this.applicantManager=applicantManager;
    }
}
