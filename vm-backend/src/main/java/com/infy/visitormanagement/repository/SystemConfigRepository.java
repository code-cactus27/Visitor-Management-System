package com.infy.visitormanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.infy.visitormanagement.entity.SystemConfig;

public interface SystemConfigRepository extends JpaRepository<SystemConfig, String> {
}