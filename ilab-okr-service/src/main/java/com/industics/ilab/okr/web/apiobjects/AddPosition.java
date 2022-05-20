package com.industics.ilab.okr.web.apiobjects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AddPosition {

    @NotBlank
    @Size(max = 128)
    private String position_name;
    @NotBlank
    @Size(max = 128)
    private String bonus_type;

    public String getPosition_name() {
        return position_name;
    }

    public String getBonus_type() {
        return bonus_type;
    }

    public void setPosition_name(String position_name) {
        this.position_name = position_name;
    }

    public void setBonus_type(String bonus_type) {
        this.bonus_type = bonus_type;
    }
}
