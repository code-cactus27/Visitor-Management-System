package com.infy.visitormanagement.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class VisitorReportRequestDTO {
    private String type;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
