package com.industics.ilab.okr.web.apiobjects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class GetApprovalByStatus {
    private int status;
    private int page_num;
    private int data_num;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setPage_num(int page_num) {
        this.page_num = page_num;
    }

    public void setData_num(int data_num) {
        this.data_num = data_num;
    }

    public int getStatus() {
        return status;
    }

    public int getPage_num() {
        return page_num;
    }

    public int getData_num() {
        return data_num;
    }
}
