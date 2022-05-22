package com.industics.ilab.okr.web.apiobjects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

public class MultiUpdateApproval {

    private int status;
    private List<String> approval_ids;

    public int getStatus() {
        return status;
    }

    public List<String> getApproval_ids() {
        return approval_ids;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setApproval_ids(List<String> approval_ids) {
        this.approval_ids = approval_ids;
    }
}
