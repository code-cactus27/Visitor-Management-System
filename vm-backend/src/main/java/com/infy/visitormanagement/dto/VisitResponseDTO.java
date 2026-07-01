package com.infy.visitormanagement.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class VisitResponseDTO {
    private Integer visitId;
    private Integer visitorId;
    private String uniqueId;
    private String visitorName;
    private String reasonForVisit;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private Integer passDuration;
    private LocalDateTime passExpiry;
    private String statusOnTime;
    private String company;
    private LocalDate visitDate;
    private LocalTime expectedTime;
}
