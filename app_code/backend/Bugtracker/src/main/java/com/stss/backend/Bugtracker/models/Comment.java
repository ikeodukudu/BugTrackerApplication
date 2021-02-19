package com.stss.backend.Bugtracker.models;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Objects;

@Entity
@Table(name="COMMENTS", uniqueConstraints = @UniqueConstraint(columnNames = "comment_id"))
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_id")
    private int commentId;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
    @JoinColumn(name="user_id", referencedColumnName = "user_id", nullable = false, updatable=false)
    private User userId;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Ticket.class)
    @JoinColumn(name="ticket_id", referencedColumnName = "ticket_id", nullable = false, updatable=false)
    private Ticket ticketId;

    @Column(nullable=false)
    private String comment;

    @Column(name="date_created", nullable=false, updatable=false)
    private Timestamp dateCreated;

    public Comment(){

    }

    public Comment(int commentId, User userId, Ticket ticketId, String comment) {
        this.commentId = commentId;
        this.userId = userId;
        this.ticketId = ticketId;
        this.comment = comment;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public User getUserId() {
        return new User(userId.getUserId(), userId.getName(), userId.getEmail());
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public Ticket getTicketId() {
        return ticketId;
    }

    public void setTicketId(Ticket ticketId) {
        this.ticketId = ticketId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment comment = (Comment) o;
        return getCommentId() == comment.getCommentId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommentId());
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", userId=" + userId +
                ", ticketId=" + ticketId +
                ", comment='" + comment + '\'' +
                '}';
    }
}
