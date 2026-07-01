package com.infy.visitormanagement.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class VisitorResponseDTO {
    private Integer visitorId;
    private String uniqueId;
    private String name;
    private String company;
    private String contactNumber;
    private String email;
    private String notes;
    private String status;
    private LocalDateTime createdAt;
}
