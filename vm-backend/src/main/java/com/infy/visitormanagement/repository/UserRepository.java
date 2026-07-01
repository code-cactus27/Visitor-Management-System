package com.infy.visitormanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.infy.visitormanagement.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String Email);
}