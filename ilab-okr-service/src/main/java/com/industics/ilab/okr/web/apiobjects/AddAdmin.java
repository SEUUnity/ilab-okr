package com.industics.ilab.okr.web.apiobjects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AddAdmin {
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

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getPermission() {
        return permission;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }
}
