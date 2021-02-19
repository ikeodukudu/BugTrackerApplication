package com.stss.backend.Bugtracker.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class TicketCreationDto {

    @NotEmpty
    private String ticketTitle;

    @NotEmpty
    private long userId;

    @NotEmpty
    private String ticketType;

    @Email
    @NotEmpty
    private String selectedUser;

    @NotEmpty
    private String ticketDescription;

    @NotEmpty
    private String priority;

    @NotEmpty
    private String status;

    public String getTicketTitle() {
        return ticketTitle;
    }

    public void setTicketTitle(String ticketTitle) {
        this.ticketTitle = ticketTitle;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public String getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(String selectedUser) {
        this.selectedUser = selectedUser;
    }

    public String getTicketDescription() {
        return ticketDescription;
    }

    public void setTicketDescription(String ticketDescription) {
        this.ticketDescription = ticketDescription;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
