package com.stss.backend.Bugtracker.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class UpdatePwdDto {

    @NotEmpty
    private String newPassword;

    @Email
    @NotEmpty
    private String email;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
