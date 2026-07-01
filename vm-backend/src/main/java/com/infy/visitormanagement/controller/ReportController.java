package com.infy.visitormanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.infy.visitormanagement.dto.VisitorReportRequestDTO;
import com.infy.visitormanagement.dto.VisitorSummaryDTO;
import com.infy.visitormanagement.dto.VisitorTrendResponseDTO;
import com.infy.visitormanagement.exception.VisitorManagementException;
import com.infy.visitormanagement.service.VisitorReportService;

@RestController
@RequestMapping("/api/admin")
public class ReportController {
    @Autowired
    private VisitorReportService visitorReportService;

    @PostMapping("/report/visitor/trends")
    public ResponseEntity<VisitorTrendResponseDTO> trends(@RequestBody VisitorReportRequestDTO req) throws VisitorManagementException {
        VisitorTrendResponseDTO res = visitorReportService.getTrends(req);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/report/visitor/summary")
    public ResponseEntity<VisitorSummaryDTO> summary(@RequestBody VisitorReportRequestDTO req) throws VisitorManagementException {
        VisitorSummaryDTO res = visitorReportService.getSummary(req);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/visit/csv")
    public ResponseEntity<byte[]> exportVisitCsv(@RequestBody VisitorReportRequestDTO req) throws VisitorManagementException {
        byte[] csvData = visitorReportService.generateCsv(req);
        String filename = "visits_report_" + req.getStartDate().toLocalDate() + "_to_" + req.getEndDate().toLocalDate() + ".csv";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(csvData);
    }
}
