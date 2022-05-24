package com.industics.ilab.okr.web.apiobjects;

import java.util.List;

public class UpdateStatus {
    private String status;
    private List<String> ids;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
