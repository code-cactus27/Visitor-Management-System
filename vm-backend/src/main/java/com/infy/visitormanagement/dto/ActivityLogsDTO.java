package com.infy.visitormanagement.dto;
import com.infy.visitormanagement.enums.LogAction;
import lombok.Data;
import java.time.LocalDateTime;
@Data
public class ActivityLogsDTO {
    private Long id;
    private String uniqueId;
    private LogAction action;
    private String message;
    private LocalDateTime timestamp;
}