package com.industics.ilab.okr.security.apiobjects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RegisterRequest {
    @NotNull
    private UserType userType = UserType.CORP;
    @NotBlank
    @Size(max = 64)
    private String username;
    @NotBlank
    @Size(max = 64)
    private String password;
}
