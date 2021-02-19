package com.stss.backend.Bugtracker.models;

import com.stss.backend.Bugtracker.enums.ERole;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="role_id")
    private Long roleId;

    @Column(nullable = false, name="role_name")
    @Enumerated(EnumType.STRING)
    private ERole roleName;

    public Role(){}

    public Role(ERole roleName){
        this.roleName = roleName;
    }

    public Role(Long roleId, ERole roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public ERole getRoleName() {
        return roleName;
    }

    public void setRoleName(ERole roleName) {
        this.roleName = roleName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role role = (Role) o;
        return getRoleId().equals(role.getRoleId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRoleId());
    }

    @Override
    public String toString() {
        return "Role{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                '}';
    }

    //private enum ro

//    public Ticket ticket;
//    public void test(){
//        roleType = ticket.setPriority("admin", "jo");
//    }

}
