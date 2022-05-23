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
    @NotBlank
    @Size(max = 128)
    private String permission;

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
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

}
