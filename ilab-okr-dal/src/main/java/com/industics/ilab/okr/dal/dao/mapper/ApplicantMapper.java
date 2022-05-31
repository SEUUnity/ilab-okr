package com.industics.ilab.okr.dal.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@Mapper
public interface ApplicantMapper {
    Map<String,Object> getApplicantByID(@Param("applicant_id") String applicant_id);

    Map<String,Object> getApplicantByBL(@Param("user_id") String user_id);
}
