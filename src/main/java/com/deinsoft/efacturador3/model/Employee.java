package com.deinsoft.efacturador3.model;

import javax.validation.constraints.*;

public class Employee {

    private Integer id;
    @NotBlank
    private String firstName;

    public void setId(Integer id) {
        this.id = id;
    }

    @NotBlank
    private String lastName;

    private String dept;

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public Integer getId() {
        return this.id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getDept() {
        return this.dept;
    }
}
