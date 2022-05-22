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

    public List<Map<String,Object>> getApprovalByStatus(int status,int page_num,int data_num){
        return approvalMapper.getApprovalByStatus(status,(page_num-1)*data_num,data_num);
    }

    public int getApprovalByStatusCount(int status){
        return approvalMapper.getApprovalByStatusCount(status);
    }

    public Map<String,Object> getApprovalByID(String approval_id){
        return approvalMapper.getApprovalByID(approval_id);
    }


}
