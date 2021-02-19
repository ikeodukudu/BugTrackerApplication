package com.stss.backend.Bugtracker.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name="users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="user_id")
    private long userId;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false)
    private long mobile;

    @Column(nullable=false)
    private String email;

    @Column(nullable=false)
    private String password;

    @Column(name="date_created", nullable=false, updatable=false)
    private Timestamp dateCreated;

    @Column(name="account_non_locked")
    private boolean accountNonLocked;

    @Column(name="failed_attempt")
    private int failedAttempt;

    @Column(name = "lock_time")
    private Date lockTime;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "role_id"))
    private Collection<Role> roles;

    public User(){

    }

//    public User(long userId, String name, long mobile, String email, String password, Timestamp dateCreated,
//                boolean accountNonLocked, int failedAttempt, Date lockTime, Collection<Role> roles) {
//        this.userId = userId;
//        this.name = name;
//        this.mobile = mobile;
//        this.email = email;
//        this.password = password;
//        this.dateCreated = dateCreated;
//        this.accountNonLocked = accountNonLocked;
//        this.failedAttempt = failedAttempt;
//        this.lockTime = lockTime;
//        this.roles = roles;
//    }

//    public User(String name, long mobile, String email, String password, Collection<Role> roles) {
//        this.name = name;
//        this.mobile = mobile;
//        this.email = email;
//        this.password = password;
//        this.roles = roles;
//    }

//    public User(String name, long mobile, String email, String password) {
//        this.name = name;
//        this.mobile = mobile;
//        this.email = email;
//        this.password = password;
//    }

    public User(long userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getMobile() {
        return mobile;
    }

    public void setMobile(long mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDateCreated() {
        return this.dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public int getFailedAttempt() {
        return failedAttempt;
    }

    public void setFailedAttempt(int failedAttempt) {
        this.failedAttempt = failedAttempt;
    }

    public Date getLockTime() {
        return lockTime;
    }

    public void setLockTime(Date lockTime) {
        this.lockTime = lockTime;
    }

    public Collection <Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection <Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getUserId() == user.getUserId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId());
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
               // ", mobile=" + mobile +
                ", email='" + email + '\'' +
                //", password='" + password + '\'' +
                //", roles=" + roles +
                '}';
    }
}
