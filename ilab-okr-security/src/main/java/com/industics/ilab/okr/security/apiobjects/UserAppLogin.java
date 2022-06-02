package com.industics.ilab.okr.security.apiobjects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserAppLogin {
    @NotBlank
    @Size(max = 256)
    private String code;
    @NotBlank
    @Size(max = 256)
    private String iv;
    @NotBlank
    @Size(max = 1024)
    private String encrypted_data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getEncryptedData() {
        return encrypted_data;
    }

    public void setEncryptedData(String encryptedData) {
        this.encrypted_data = encryptedData;
    }

//    public String getEn() {
//        return en;
//    }
//
//    public void setEn(String en) {
//        this.en = en;
//    }
}
