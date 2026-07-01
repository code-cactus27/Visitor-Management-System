package com.infy.visitormanagement.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class VisitRequestDTO {
    private Integer visitId;
    @NotBlank(message = "UniqueId is required")
    private String uniqueId;
    @NotBlank(message = "Reason for visit is required")
    private String reasonForVisit;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Minimum value required is 1")
    private Integer passDuration;
    private LocalDateTime passExpiry;
    private String statusOnTime;
    private LocalDate visitDate;
    private LocalTime expectedTime;
}
