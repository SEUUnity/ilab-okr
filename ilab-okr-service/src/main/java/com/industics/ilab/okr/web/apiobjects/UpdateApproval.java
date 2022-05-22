package com.industics.ilab.okr.web.apiobjects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UpdateApproval {
    private String status;
    @NotBlank
    @Size(max = 128)
    private String approval_id;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setApproval_id(String approval_id) {
        this.approval_id = approval_id;
    }

    public String getApproval_id() {
        return approval_id;
    }

}
