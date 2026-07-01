package com.infy.visitormanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FeedbackDTO {
    @NotBlank
    private String visitorId;
    @NotBlank
    @Size(min = 3, max = 50)
    private String visitorName;
    @NotBlank
    private String feedbackText;
    @NotBlank
    private Integer rating;
}
