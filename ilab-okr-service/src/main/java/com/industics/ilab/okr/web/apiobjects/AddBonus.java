package com.industics.ilab.okr.web.apiobjects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AddBonus {
    private int amount;
    @NotBlank
    @Size(max = 128)
    private String bonus_type;

    public int getAmount() {
        return amount;
    }

    public String getBonus_type() {
        return bonus_type;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setBonus_type(String bonus_type) {
        this.bonus_type = bonus_type;
    }
}
