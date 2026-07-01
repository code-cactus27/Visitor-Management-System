package com.infy.visitormanagement.service;

import com.infy.visitormanagement.dto.VisitRequestDTO;
import com.infy.visitormanagement.dto.VisitResponseDTO;
import com.infy.visitormanagement.dto.VisitorWithVisitRequestDTO;
import com.infy.visitormanagement.entity.ActivityLogs;
import com.infy.visitormanagement.entity.VisitRecord;
import com.infy.visitormanagement.entity.Visitor;
import com.infy.visitormanagement.enums.LogAction;
import com.infy.visitormanagement.enums.VisitorStatus;
import com.infy.visitormanagement.repository.ActivityLogsRepository;
import com.infy.visitormanagement.repository.VisitRecordRepository;
import com.infy.visitormanagement.repository.VisitorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class VisitServiceImpl implements VisitService {

    @Autowired
    private VisitorRepository visitorRepository;

    @Autowired
    private VisitRecordRepository visitRecordRepository;

    @Autowired
    private VisitorService visitorService;

    @Autowired
    private ActivityLogsRepository activityLogsRepository;

    // ModelMapper is still used for entity → entity / dto → entity mappings.
    // It is NO LONGER used for VisitRecord → VisitResponseDTO because ModelMapper
    // can't disambiguate between Visitor.getUniqueId() and Visitor.getVisitorId()
    // when trying to populate VisitResponseDTO.setVisitorId().
    // Use mapToVisitResponseDTO() instead.
    ModelMapper modelMapper = new ModelMapper();

    // ─────────────────────────────────────────────────────────────────────────
    // Private helper: explicit, unambiguous VisitRecord → VisitResponseDTO
    // mapping. Replaces every modelMapper.map(visit, VisitResponseDTO.class)
    // call in this service.
    // ─────────────────────────────────────────────────────────────────────────
    private VisitResponseDTO mapToVisitResponseDTO(VisitRecord visit) {
        Visitor visitor = visit.getVisitor();

        VisitResponseDTO dto = new VisitResponseDTO();

        // ── Visit fields ──────────────────────────────────────────────────────
        dto.setVisitId(visit.getVisitId());
        dto.setReasonForVisit(visit.getReasonForVisit());
        dto.setEntryTime(visit.getEntryTime());
        dto.setExitTime(visit.getExitTime());
        dto.setPassDuration(visit.getPassDuration());
        dto.setPassExpiry(visit.getPassExpiry());
        dto.setVisitDate(visit.getVisitDate());
        dto.setExpectedTime(visit.getExpectedTime());
        dto.setStatusOnTime(
                visit.getStatusOnTime() != null
                        ? visit.getStatusOnTime().name()
                        : null
        );

        // ── Visitor fields (manually set — avoids ModelMapper ambiguity) ──────
        dto.setVisitorId(visitor.getVisitorId());    // integer PK — needed for gate-pass URL
        dto.setUniqueId(visitor.getUniqueId());      // string e.g. "260502K20653"
        dto.setVisitorName(visitor.getName());
        dto.setCompany(visitor.getCompany());

        return dto;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Add new visitor + first visit
    // ─────────────────────────────────────────────────────────────────────────
    @Override
    public String addVisitNew(VisitorWithVisitRequestDTO dto) {
        Visitor visitor = modelMapper.map(dto, Visitor.class);
        visitor.setUniqueId(
                visitorService.generateUniqueId(dto.getName(), dto.getContactNumber())
        );
        visitor.setCreatedAt(LocalDateTime.now());

        VisitRecord visit = new VisitRecord();
        visit.setReasonForVisit(dto.getReasonForVisit());
        visit.setPassDuration(dto.getPassDuration());
        visit.setExpectedTime(dto.getExpectedTime());
        visit.setVisitDate(dto.getVisitDate());

        LocalDateTime visitDateTime = LocalDateTime.of(
                dto.getVisitDate(),
                dto.getExpectedTime()
        );
        visit.setPassExpiry(visitDateTime.plusHours(dto.getPassDuration()));
        visit.setEntryTime(null);
        visit.setExitTime(null);
        visit.setStatusOnTime(VisitorStatus.PENDING);

        visit.setVisitor(visitor);
        visitor.getVisitRecord().add(visit);

        visitorRepository.save(visitor);
        logActivity(visitor.getUniqueId(), LogAction.VISITOR_REGISTERED, "New visitor added with visit");

        return "New visitor + visit added. UniqueId: " + visitor.getUniqueId();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Add visit for existing visitor
    // ─────────────────────────────────────────────────────────────────────────
    @Override
    public String addVisitExisting(VisitRequestDTO dto) {
        Visitor visitor = visitorRepository.getByUniqueId(dto.getUniqueId());

        VisitRecord visit = modelMapper.map(dto, VisitRecord.class);

        visit.setExpectedTime(dto.getExpectedTime());
        visit.setVisitDate(dto.getVisitDate());
        visit.setPassDuration(dto.getPassDuration());

        LocalDateTime visitDateTime = LocalDateTime.of(
                dto.getVisitDate(),
                dto.getExpectedTime()
        );
        visit.setPassExpiry(visitDateTime.plusHours(dto.getPassDuration()));
        visit.setEntryTime(null);
        visit.setExitTime(null);
        visit.setStatusOnTime(VisitorStatus.PENDING);
        visit.setVisitor(visitor);

        visitRecordRepository.save(visit);
        logActivity(visitor.getUniqueId(), LogAction.VISIT_ADDED, "Visit added for existing visitor");

        return "Visit added for existing visitor: " + dto.getUniqueId();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Get all visits for a single visitor (by uniqueId)
    // ─────────────────────────────────────────────────────────────────────────
    @Override
    public List<VisitResponseDTO> getVisitsByVisitorUniqueId(String uniqueId) {
        visitorRepository.getByUniqueId(uniqueId); // validates visitor exists

        List<VisitRecord> visits = visitRecordRepository
                .findByVisitor_UniqueIdOrderByVisitDateDescExpectedTimeDesc(uniqueId);

        List<VisitResponseDTO> response = new ArrayList<>();
        for (VisitRecord visit : visits) {
            response.add(mapToVisitResponseDTO(visit));
        }
        return response;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Get all visits (security officer table)
    // ─────────────────────────────────────────────────────────────────────────
    @Override
    public List<VisitResponseDTO> getAllVisits() {
        List<VisitRecord> visits = visitRecordRepository.findAll();

        // Auto-expire / auto-checkout stale records
        for (VisitRecord visit : visits) {
            if (visit.getStatusOnTime() == VisitorStatus.CHECKED_IN
                    && visit.getExitTime() != null
                    && LocalDateTime.now().isAfter(visit.getExitTime())) {
                visit.setStatusOnTime(VisitorStatus.CHECKED_OUT);
                visitRecordRepository.save(visit);
            }
            if (visit.getStatusOnTime() == VisitorStatus.PENDING
                    && visit.getPassExpiry() != null
                    && LocalDateTime.now().isAfter(visit.getPassExpiry())) {
                visit.setStatusOnTime(VisitorStatus.EXPIRED);
                visitRecordRepository.save(visit);
            }
        }

        // Sort: PENDING → CHECKED_IN → CHECKED_OUT → EXPIRED, then by date/time
        visits.sort((a, b) -> {
            int statusCompare = getStatusPriority(a.getStatusOnTime())
                    - getStatusPriority(b.getStatusOnTime());
            if (statusCompare != 0) return statusCompare;

            int dateCompare = a.getVisitDate().compareTo(b.getVisitDate());
            if (dateCompare != 0) return dateCompare;

            return a.getExpectedTime().compareTo(b.getExpectedTime());
        });

        List<VisitResponseDTO> response = new ArrayList<>();
        for (VisitRecord visit : visits) {
            response.add(mapToVisitResponseDTO(visit));
        }
        return response;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Dashboard counts
    // ─────────────────────────────────────────────────────────────────────────
    @Override
    public Map<String, Integer> getDashboardData() {
        Map<String, Integer> data = new HashMap<>();
        data.put("totalVisitors", visitRecordRepository.countTotalVisitors());
        data.put("expectedToday", visitRecordRepository.countTodayVisitors());
        data.put("checkedIn", visitRecordRepository.countCheckedIn());
        data.put("checkedOut", visitRecordRepository.countCheckedOut());
        data.put("pending", visitRecordRepository.countPending());
        data.put("expired", visitRecordRepository.countExpired());
        return data;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Approve (check-in)
    // ─────────────────────────────────────────────────────────────────────────
    @Override
    public VisitRecord approveVisitor(Integer visitId) {
        VisitRecord visit = visitRecordRepository.findById(visitId)
                .orElseThrow(() -> new RuntimeException("Visitor not found"));

        LocalDateTime entryTime = LocalDateTime.now();
        visit.setEntryTime(entryTime);
        visit.setPassExpiry(entryTime.plusHours(visit.getPassDuration()));
        visit.setStatusOnTime(VisitorStatus.CHECKED_IN);

        logActivity(visit.getVisitor().getUniqueId(), LogAction.CHECKED_IN, "Visitor checked in");
        return visitRecordRepository.save(visit);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Reject
    // ─────────────────────────────────────────────────────────────────────────
    @Override
    public void rejectVisitor(Integer id) {
        VisitRecord visit = visitRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Visit not found"));

        visit.setStatusOnTime(VisitorStatus.EXPIRED);
        visit.setEntryTime(null);
        visit.setExitTime(null);
        visit.setPassExpiry(null);

        logActivity(visit.getVisitor().getUniqueId(), LogAction.VISITOR_REJECTED, "Visitor Rejected");
        visitRecordRepository.save(visit);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Check-out
    // ─────────────────────────────────────────────────────────────────────────
    @Override
    public void checkOutVisitor(Integer id) {
        VisitRecord visit = visitRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Visit not found"));

        visit.setStatusOnTime(VisitorStatus.CHECKED_OUT);
        visit.setExitTime(LocalDateTime.now());

        visitRecordRepository.save(visit);
        logActivity(visit.getVisitor().getUniqueId(), LogAction.CHECKED_OUT, "Visitor checked out");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────────────
    private int getStatusPriority(VisitorStatus status) {
        switch (status) {
            case PENDING:
                return 1;
            case CHECKED_IN:
                return 2;
            case CHECKED_OUT:
                return 3;
            case EXPIRED:
                return 4;
            default:
                return 5;
        }
    }

    private void logActivity(String uniqueId, LogAction action, String message) {
        ActivityLogs log = new ActivityLogs(
                uniqueId,
                action,
                message,
                LocalDateTime.now()
        );
        activityLogsRepository.save(log);
    }
}