package com.stss.backend.Bugtracker.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.stss.backend.Bugtracker.enums.Priority;
import com.stss.backend.Bugtracker.enums.Status;
import com.stss.backend.Bugtracker.enums.TicketType;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name="TICKETS", uniqueConstraints = @UniqueConstraint(columnNames = "ticket_id"))
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ticket_id")
    private int ticketId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, targetEntity=User.class)
    @JoinColumn(name="user_id", referencedColumnName = "user_id", nullable = false, updatable=false)
    private User userId;

    @Enumerated(EnumType.STRING)
    @Column(name="ticket_type", nullable = false, updatable=false)
    private TicketType ticketType;

    @Column(name="ticket_name", nullable=false)
    private String ticketName;

    @Column(name="ticket_description", nullable=false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity=User.class)
    @JoinColumn(name="assigned_to", referencedColumnName = "user_id")
    private User assignedTo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Status status;

    @Column(name="date_created", nullable=false, updatable=false)
    private Timestamp dateCreated;

    public Ticket(){

    }

    public Ticket(int ticketId, User userId, TicketType ticketType, String ticketName, String description,
                  User assignedTo, Priority priority, Status status) {
        User user = new User(userId.getUserId(), userId.getName(), userId.getEmail());
        User assigned = new User(assignedTo.getUserId(), assignedTo.getName(), user.getEmail());
        this.ticketId = ticketId;
        this.userId = user;
        this.ticketType = ticketType;
        this.ticketName = ticketName;
        this.description = description;
        this.assignedTo = assigned;
        this.priority = priority;
        this.status = status;
    }

    public Ticket(User userId, TicketType ticketType, String ticketName, String description, User assignedTo, Priority priority, Status status) {
        this.userId = userId;
        this.ticketType = ticketType;
        this.ticketName = ticketName;
        this.description = description;
        this.assignedTo = assignedTo;
        this.priority = priority;
        this.status = status;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public User getUserId() {
        return new User(userId.getUserId(), userId.getName(), userId.getEmail());
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    public User getAssignedTo() {
        return new User(assignedTo.getUserId(), assignedTo.getName(), assignedTo.getEmail());
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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
        if (!(o instanceof Ticket)) return false;
        Ticket ticket = (Ticket) o;
        return getTicketId() == ticket.getTicketId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTicketId());
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId=" + ticketId +
                ", userId=" + userId +
                //", userId=" + userId.getUserId() +
                ", ticketType=" + ticketType +
                ", ticketName='" + ticketName + '\'' +
                ", description='" + description + '\'' +
                ", assignedTo=" + assignedTo +
                ", priority=" + priority +
                ", status=" + status +
                '}';
    }
}
