package com.infy.visitormanagement.service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.infy.visitormanagement.dto.TrendDataDTO;
import com.infy.visitormanagement.dto.VisitorReportDTO;
import com.infy.visitormanagement.dto.VisitorReportRequestDTO;
import com.infy.visitormanagement.dto.VisitorSummaryDTO;
import com.infy.visitormanagement.dto.VisitorTrendResponseDTO;
import com.infy.visitormanagement.exception.VisitorManagementException;
import com.infy.visitormanagement.repository.VisitRecordRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class VisitorReportServiceImpl implements VisitorReportService {
    @Autowired
    private VisitRecordRepository visitRecordRepository;

    @Override
    public VisitorTrendResponseDTO getTrends(VisitorReportRequestDTO req) throws VisitorManagementException {
        List<Object[]> result;
        switch (req.getType()) {
            case "daily":
                result = visitRecordRepository.getDaily(req.getStartDate(), req.getEndDate());
                break;
            case "weekly":
                result = visitRecordRepository.getWeekly(req.getStartDate(), req.getEndDate());
                break;
            case "monthly":
                result = visitRecordRepository.getMonthly(req.getStartDate(), req.getEndDate());
                break;
            default:
                throw new VisitorManagementException("service.TYPE_INVALID");
        }
        List<TrendDataDTO> data = result.stream().map(r -> new TrendDataDTO(r[0].toString(), ((Number) r[1]).longValue())).toList();
        VisitorTrendResponseDTO res = new VisitorTrendResponseDTO();
        res.setType(req.getType());
        res.setData(data);
        return res;
    }

    @Override
    public VisitorSummaryDTO getSummary(VisitorReportRequestDTO req) throws VisitorManagementException {
        Long total = visitRecordRepository.getTotal(req.getStartDate(), req.getEndDate());
        Double avgSec = visitRecordRepository.getAvg(req.getStartDate(), req.getEndDate());
        return new VisitorSummaryDTO(total, formatTime(avgSec));
    }

    private String formatTime(Double sec) {
        if (sec == null) return "00:00:00";
        long s = sec.longValue();
        long h = s / 3600;
        long m = (s % 3600) / 60;
        long se = s % 60;
        return String.format("%02d:%02d:%02d", h, m, se);
    }

    public byte[] generateCsv(VisitorReportRequestDTO req) throws VisitorManagementException {
        if (req.getStartDate().isAfter(req.getEndDate())) {
            throw new VisitorManagementException("Service.INVALID_START_DATE");
        }
        List<VisitorReportDTO> records = visitRecordRepository.getReportData(req.getStartDate(), req.getEndDate());
//                                Build CSV
        StringBuilder csv = new StringBuilder();
        csv.append("Name,Email,Contact Number,Entry Time,Exit Time,Duration\n");
        for (VisitorReportDTO r : records) {
            csv.append(escapeCsv(r.getName())).append(",")
                    .append(escapeCsv(r.getEmail())).append(",")
                    .append(escapeCsv(r.getContactNumber())).append(",")
                    .append(escapeCsv(formatDateTime(r.getEntryTime()))).append(",")
                    .append(escapeCsv(formatDateTime(r.getExitTime()))).append(",")
                    .append(calculateDuration(r.getEntryTime(), r.getExitTime()))
                    .append("\n");
        }
        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private String formatDateTime(LocalDateTime dt) {
        return dt == null ? "" : dt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private String calculateDuration(LocalDateTime entry, LocalDateTime exit) {
        if (entry == null || exit == null) return "";
        long minutes = Duration.between(entry, exit).toMinutes();
        if (minutes < 0) return "0";
        return (minutes / 60) + "h " + (minutes % 60) + "m";
    }
}
