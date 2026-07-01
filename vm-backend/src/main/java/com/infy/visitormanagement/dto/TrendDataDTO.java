package com.infy.visitormanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TrendDataDTO {
    private String label;
    private Long count;
}
