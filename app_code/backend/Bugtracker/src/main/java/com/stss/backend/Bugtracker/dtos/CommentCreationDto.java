package com.stss.backend.Bugtracker.dtos;

import javax.validation.constraints.NotEmpty;

public class CommentCreationDto {

    @NotEmpty
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

//    public CommentCreationDto(@NotEmpty String comment) {
//        this.comment = comment;
//    }
}
