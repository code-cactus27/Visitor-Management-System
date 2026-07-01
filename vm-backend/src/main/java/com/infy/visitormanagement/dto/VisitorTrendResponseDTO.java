package com.infy.visitormanagement.dto;
import java.util.List;
import lombok.Data;
@Data
public class VisitorTrendResponseDTO {
    private String type;
    private List<TrendDataDTO> data;
}
