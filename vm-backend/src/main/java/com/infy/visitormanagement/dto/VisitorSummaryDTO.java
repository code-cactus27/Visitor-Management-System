package com.infy.visitormanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VisitorSummaryDTO {
    private Long totalVisitors;
    private String avgVisitDuration;
}
