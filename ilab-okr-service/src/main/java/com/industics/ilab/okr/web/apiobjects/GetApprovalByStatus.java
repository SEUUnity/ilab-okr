package com.industics.ilab.okr.web.apiobjects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

public class GetApprovalByStatus {
    private List<String> status;
    private int page_num;
    private int data_num;


    public void setPage_num(int page_num) {
        this.page_num = page_num;
    }

    public void setData_num(int data_num) {
        this.data_num = data_num;
    }

    public List<String> getStatus() {
        return status;
    }

    public void setStatus(List<String> status) {
        this.status = status;
    }

    public int getPage_num() {
        return page_num;
    }

    public int getData_num() {
        return data_num;
    }
}
