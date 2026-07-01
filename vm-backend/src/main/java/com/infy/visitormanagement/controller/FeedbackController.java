package com.infy.visitormanagement.controller;
import com.infy.visitormanagement.dto.FeedbackDTO;
import com.infy.visitormanagement.entity.Feedback;
import com.infy.visitormanagement.service.FeedbackService;
import jakarta.validation.constraints.AssertTrue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedback")
@CrossOrigin(origins = "http://localhost:4200/*")
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;
    @PostMapping("/submit")
    public String submitFeedback(@RequestBody FeedbackDTO dto){
        return feedbackService.saveFeedback(dto);
    }
    @GetMapping("/all")
    public List<Feedback> getAllFeedbacks(){
        return feedbackService.getAllFeedbacks();
    }
}
