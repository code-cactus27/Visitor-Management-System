package com.infy.visitormanagement.service;

import com.infy.visitormanagement.dto.GatePassResponseDTO;
import com.infy.visitormanagement.entity.ActivityLogs;
import com.infy.visitormanagement.entity.VisitRecord;
import com.infy.visitormanagement.entity.Visitor;
import com.infy.visitormanagement.enums.LogAction;
import com.infy.visitormanagement.exception.ResourceNotFoundException;
import com.infy.visitormanagement.exception.VisitorManagementException;
import com.infy.visitormanagement.repository.ActivityLogsRepository;
import com.infy.visitormanagement.repository.VisitRecordRepository;
import com.infy.visitormanagement.repository.VisitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class GatePassServiceImpl implements GatePassService {
    @Autowired
    private VisitorRepository visitorRepository;
    @Autowired
    private VisitRecordRepository visitRecordRepository;
    @Autowired
    private ActivityLogsRepository activityLogsRepository;

    private void logActivity(String uniqueId, LogAction action, String message) {
        ActivityLogs log = new ActivityLogs(
                uniqueId,
                action,
                message,
                LocalDateTime.now()
        );
        activityLogsRepository.save(log);
    }

    @Override
    public GatePassResponseDTO buildGatePass(Integer visitorId, Integer visitId) throws VisitorManagementException {
        // 1. Fetch Visitor by primary key
        Visitor visitor = visitorRepository.findById(visitorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "GatePassService.VISITOR_NOT_FOUND"));
        // 2. Fetch VisitRecord by primary key
        VisitRecord visit = visitRecordRepository.findById(visitId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "GatePassService.VISIT_NOT_FOUND"));
        // 3. Verify ownership
        if (!visit.getVisitor().getVisitorId().equals(visitorId)) {
            throw new VisitorManagementException(
                    "GatePassService.VISIT_VISITOR_MISMATCH");
        }
        // 4. Build flat DTO
        GatePassResponseDTO dto = new GatePassResponseDTO();
        // Visitor fields
        dto.setVisitorId(visitor.getVisitorId());
        dto.setUniqueId(visitor.getUniqueId());
        dto.setName(visitor.getName());
        dto.setCompany(visitor.getCompany());
        dto.setContactNumber(visitor.getContactNumber());
        dto.setEmail(visitor.getEmail());
        dto.setNotes(visitor.getNotes());
        dto.setCreatedAt(visitor.getCreatedAt());
        // Visit fields
        dto.setVisitId(visit.getVisitId());
        dto.setReasonForVisit(visit.getReasonForVisit());
        dto.setVisitDate(visit.getVisitDate());
        dto.setExpectedTime(visit.getExpectedTime());
        dto.setPassDuration(visit.getPassDuration());
        dto.setEntryTime(visit.getEntryTime());
        dto.setExitTime(visit.getExitTime());
        dto.setPassExpiry(visit.getPassExpiry());
        // Status: enum → String (null-safe)
        dto.setStatus(
                visit.getStatusOnTime() != null
                        ? visit.getStatusOnTime().name()
                        : "PENDING"
        );

        logActivity(visit.getVisitor().getUniqueId(), LogAction.GATEPASS_GENERATED, "Gatepass Generated");

        return dto;


    }

}