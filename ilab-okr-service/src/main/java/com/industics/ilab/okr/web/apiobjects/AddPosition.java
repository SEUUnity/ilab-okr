package com.industics.ilab.okr.web.apiobjects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AddPosition {

    @NotBlank
    @Size(max = 128)
    private String position_name;
    @NotBlank
    @Size(max = 2048)
    private String position_detail;
    @NotBlank
    @Size(max = 4096)
    private String position_require;
    @NotBlank
    @Size(max = 128)
    private String salary;
    private int salary_count;
    private int quota;
    @NotBlank
    @Size(max = 128)
    private String degree;
    @NotBlank
    @Size(max = 128)
    private String location;
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

    public String getPosition_detail() {
        return position_detail;
    }

    public void setPosition_detail(String position_detail) {
        this.position_detail = position_detail;
    }

    public String getPosition_require() {
        return position_require;
    }

    public void setPosition_require(String position_require) {
        this.position_require = position_require;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public int getSalary_count() {
        return salary_count;
    }

    public void setSalary_count(int salary_count) {
        this.salary_count = salary_count;
    }

    public int getQuota() {
        return quota;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
