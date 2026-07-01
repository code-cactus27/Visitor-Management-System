package com.infy.visitormanagement.service;

import com.infy.visitormanagement.dto.FeedbackDTO;
import com.infy.visitormanagement.entity.Feedback;
import com.infy.visitormanagement.repository.FeedbackRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class FeedbackServiceImpl implements FeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;

    @Override
    public String saveFeedback(FeedbackDTO feedbackDTO) {
        Feedback feedback = new Feedback();
        feedback.setVisitorId(feedbackDTO.getVisitorId());
        feedback.setVisitorName(feedbackDTO.getVisitorName());
        feedback.setFeedbackText(feedbackDTO.getFeedbackText());
        feedback.setRating(feedbackDTO.getRating());
        feedback.setCreatedAt(LocalDateTime.now());
        feedbackRepository.save(feedback);
        return "Feedback saved successfully!";
    }

    @Override
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }
}
