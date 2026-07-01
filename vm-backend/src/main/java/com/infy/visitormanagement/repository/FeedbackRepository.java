package com.infy.visitormanagement.repository;

import com.infy.visitormanagement.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
}
