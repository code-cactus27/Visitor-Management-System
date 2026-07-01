package com.infy.visitormanagement.service;

import com.infy.visitormanagement.dto.VisitorReportRequestDTO;
import com.infy.visitormanagement.dto.VisitorSummaryDTO;
import com.infy.visitormanagement.dto.VisitorTrendResponseDTO;
import com.infy.visitormanagement.exception.VisitorManagementException;

public interface VisitorReportService {
    VisitorTrendResponseDTO getTrends(VisitorReportRequestDTO req) throws VisitorManagementException;

    byte[] generateCsv(VisitorReportRequestDTO req) throws VisitorManagementException;

    VisitorSummaryDTO getSummary(VisitorReportRequestDTO req) throws VisitorManagementException;
}
