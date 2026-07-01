package com.infy.visitormanagement.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VisitorReportDTO {
    private String name;
    private String email;
    private String contactNumber;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
}