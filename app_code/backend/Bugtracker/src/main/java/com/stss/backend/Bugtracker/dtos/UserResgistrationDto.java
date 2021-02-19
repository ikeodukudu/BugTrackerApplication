package com.stss.backend.Bugtracker.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;

public class UserResgistrationDto {

    @NotEmpty
    private String name;

    @NotEmpty
    private String password;

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    private long mobile;

    private Collection<String> role;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getMobile() {
        return mobile;
    }

    public void setMobile(long mobile) {
        this.mobile = mobile;
    }

    public Collection<String> getRole() {
        return role;
    }

    public void setRole(Collection<String> role) {
        this.role = role;
    }
}
