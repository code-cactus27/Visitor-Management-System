package com.infy.visitormanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.infy.visitormanagement.entity.Visitor;

public interface VisitorRepository extends JpaRepository<Visitor, Integer> {
    public List<Visitor> findAllByOrderByCreatedAtDesc();

    public Visitor getByUniqueId(String uniqueId);
}