package com.industics.ilab.okr.dal.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Mapper
public interface ApplicantMapper {
    Map<String,Object> getApplicantByID(@Param("applicant_id") String applicant_id);

    List<Map<String,Object>> getApplicantByBL(@Param("user_id") String user_id);
    void addApplicant(@Param("user_id") String user_id,
                      @Param("name") String name,
                      @Param("phone") String phone,
                      @Param("position_id") String position_id,
                      @Param("resume") String resume,
                      @Param("work_num") String work_num);
    void updateResume(@Param("applicant_id") String applicant_id,
                      @Param("resume") String resume);
    Map<String,Object>getResume(@Param("applicant_id") String applicant);

    List<Map<String,Object>> getApplicants(@Param("status") List<Integer> status,
                                    @Param("start") int start,
                                    @Param("end") int end);
    int getApplicantsCount(@Param("status") List<Integer> status);
    void updateApplicantStatus(@Param("ids") List<String>ids,
                          @Param("status") int status);
}
