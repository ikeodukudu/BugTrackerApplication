package com.stss.backend.Bugtracker.dtos;

public class HomeDto {

    private String message;

    public HomeDto(String message) {
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "{" +
                "message='" + message + '\'' +
                '}';
    }
}
