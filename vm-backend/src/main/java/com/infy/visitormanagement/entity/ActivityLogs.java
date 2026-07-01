package com.infy.visitormanagement.entity;

import com.infy.visitormanagement.enums.LogAction;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "activity_logs")
public class ActivityLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long id;
    @Column(name = "unique_id")
    private String uniqueId;
    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false)
    private LogAction action;
    @Column(name = "message", columnDefinition = "TEXT")
    private String message;
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    public ActivityLogs() {}
    public ActivityLogs(String uniqueId,
                        LogAction action,
                        String message,
                        LocalDateTime timestamp) {
        this.uniqueId  = uniqueId;
        this.action    = action;
        this.message   = message;
        this.timestamp = timestamp;
    }
}