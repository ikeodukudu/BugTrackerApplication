package com.stss.backend.Bugtracker.repositories;

import com.stss.backend.Bugtracker.enums.ERole;
import com.stss.backend.Bugtracker.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(ERole roleName);
}
