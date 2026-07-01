package com.infy.visitormanagement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class GatePassResponseDTO {
    // ── Visitor ──────────────────────────────────────────────────────────────
    private Integer visitorId;
    private String uniqueId;
    private String name;
    private String company;
    private String contactNumber;
    private String email;
    private String notes;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    // ── Visit ─────────────────────────────────────────────────────────────────
    private Integer visitId;
    private String reasonForVisit;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate visitDate;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime expectedTime;
    private Integer passDuration;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime entryTime;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime exitTime;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime passExpiry;
    /**
     * PENDING | CHECKED_IN | CHECKED_OUT | EXPIRED
     */
    private String status;
}
