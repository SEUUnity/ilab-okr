package com.industics.ilab.okr.dal.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface ApprovalMapper {

//    void addApproval(@Param("approval_id") String applicant_id,
//                     @Param("user_id") String user_id);

    void updateApproval(@Param("approval_id") String approval_id,
                     @Param("status") int status);

    List<Map<String,Object>> getApprovalByStatus(@Param("status") List<Integer> status,
                                                 @Param("start") int start,
                                                 @Param("end") int end);

    int getApprovalByStatusCount(@Param("status") List<Integer>  status);

    List<Map<String,Object>> getApproval(@Param("start") int start,
                                         @Param("end") int end);

    int getApprovalCount();

    Map<String,Object> getInfoByApprovalId(@Param("approval_id") String approval_id);

    Map<String,Object> getApprovalByID(@Param("approval_id") String approval_id);

    List<Map<String,Object>> ranking(@Param("style") int style);
    List<Map<String,Object>> myRanking(@Param("style") int style);
}
