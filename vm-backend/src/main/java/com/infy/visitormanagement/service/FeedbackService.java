package com.infy.visitormanagement.service;

import com.infy.visitormanagement.dto.FeedbackDTO;
import com.infy.visitormanagement.entity.Feedback;

import java.util.List;

public interface FeedbackService {
    String saveFeedback(FeedbackDTO feedbackDto);

    List<Feedback> getAllFeedbacks();
}
