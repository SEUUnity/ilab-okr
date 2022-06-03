package com.industics.ilab.okr.dal.manager;

import com.industics.ilab.okr.dal.dao.mapper.ApprovalMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ApprovalManager {
    private ApprovalMapper approvalMapper;
    @Autowired
    public void setApprovalMapper(ApprovalMapper approvalMapper){this.approvalMapper=approvalMapper;}

    public void updateApproval(String approval_id,int status){
        approvalMapper.updateApproval(approval_id,status);
    }

    public List<Map<String,Object>> getApprovalByStatus(List<Integer>  status,int page_num,int data_num){
        return approvalMapper.getApprovalByStatus(status,(page_num-1)*data_num,data_num);
    }

    public Map<String,Object> getInfoByApprovalId(String approval_id){
        return approvalMapper.getInfoByApprovalId(approval_id);
    }

    public int getApprovalByStatusCount(List<Integer> status){
        return approvalMapper.getApprovalByStatusCount(status);
    }

    public List<Map<String,Object>> getApproval(int page_num,int data_num){
        return approvalMapper.getApproval((page_num-1)*data_num,data_num);
    }

    public int getApprovalCount(){
        return approvalMapper.getApprovalCount();
    }

    public Map<String,Object> getApprovalByID(String approval_id){
        return approvalMapper.getApprovalByID(approval_id);
    }

    public List<Map<String,Object>> ranking(int style){
        return approvalMapper.ranking(style);
    }
    public List<Map<String,Object>> myRanking(int style){
        return approvalMapper.myRanking(style);
    }

    public List<Map<String,Object>> getApprovalByBL(String user_id){
        return approvalMapper.getApprovalByBL(user_id);
    }






}
