package com.industics.ilab.okr.web.apiobjects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UpdateUser {
    @NotBlank
    @Size(max = 128)
    private String open_id;
    @NotBlank
    @Size(max = 128)
    private String name;
    @NotBlank
    @Size(max = 128)
    private String work_num;
    @NotBlank
    @Size(max = 128)
    private String we_chat;

    public String getOpen_id() {
        return open_id;
    }

    public void setOpen_id(String open_id) {
        this.open_id = open_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWork_num() {
        return work_num;
    }

    public void setWork_num(String work_num) {
        this.work_num = work_num;
    }

    public String getWe_chat() {
        return we_chat;
    }

    public void setWe_chat(String we_chat) {
        this.we_chat = we_chat;
    }
}
