package com.infy.visitormanagement.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.infy.visitormanagement.enums.VisitorStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import jakarta.persistence.Table;

@Entity
@Table(name = "visit_record")
@Data
public class VisitRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "visit_id")
    private Integer visitId;
    @Column(name = "reason_for_visit", nullable = false, length = 255)
    private String reasonForVisit;
    @Column(name = "entry_time")
    private LocalDateTime entryTime;
    @Column(name = "exit_time")
    private LocalDateTime exitTime;
    @Column(name = "pass_duration", nullable = false)
    private Integer passDuration;
    @Column(name = "pass_expiry")
    private LocalDateTime passExpiry;
    @Enumerated(EnumType.STRING)
    @Column(name = "status_on_time", length = 20)
    private VisitorStatus statusOnTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visitor_id", referencedColumnName = "visitor_id", nullable = false)
    private Visitor visitor;
    @Column(name = "visit_date")
    private LocalDate visitDate;
    @Column(name = "expected_time")
    private LocalTime expectedTime;
}