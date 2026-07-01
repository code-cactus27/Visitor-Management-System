package com.infy.visitormanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.infy.visitormanagement.entity.Role;
import com.infy.visitormanagement.enums.RoleType;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(RoleType roleName);
}