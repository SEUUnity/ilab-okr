package com.industics.ilab.okr.security.apiobjects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserRegister {
    @NotBlank
    @Size(max = 64)
    private String work_num;
    @NotBlank
    @Size(max = 64)
    private String name;
    @NotBlank
    @Size(max = 64)
    private String email;
    @NotBlank
    @Size(max = 64)
    private String password;
    @NotBlank
    @Size(max = 64)
    private String phone;
    @NotBlank
    @Size(max = 64)
    private String we_chat;
    @NotBlank
    @Size(max = 64)
    private String code;

    public String getWork_num() {
        return work_num;
    }

    public void setWork_num(String work_num) {
        this.work_num = work_num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWe_chat() {
        return we_chat;
    }

    public void setWe_chat(String we_chat) {
        this.we_chat = we_chat;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
