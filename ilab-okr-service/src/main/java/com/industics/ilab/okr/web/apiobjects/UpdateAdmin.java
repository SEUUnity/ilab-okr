package com.industics.ilab.okr.web.apiobjects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UpdateAdmin {
    @NotBlank
    @Size(max = 128)
    private String admin_id;
    @NotBlank
    @Size(max = 128)
    private String name;
    @NotBlank
    @Size(max = 128)
    private String username;
    @NotBlank
    @Size(max = 128)
    private String password;
    private int permission;


    public String getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(String admin_id) {
        this.admin_id = admin_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

}
