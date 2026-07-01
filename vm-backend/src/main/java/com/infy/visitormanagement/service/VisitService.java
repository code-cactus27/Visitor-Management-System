package com.infy.visitormanagement.service;

import java.util.List;
import java.util.Map;

import com.infy.visitormanagement.dto.VisitRequestDTO;
import com.infy.visitormanagement.dto.VisitResponseDTO;
import com.infy.visitormanagement.dto.VisitorWithVisitRequestDTO;
import com.infy.visitormanagement.entity.VisitRecord;

public interface VisitService {
    String addVisitNew(VisitorWithVisitRequestDTO visitorWithVisitRequestDTO);

    String addVisitExisting(VisitRequestDTO visitRequestDTO);

    List<VisitResponseDTO> getVisitsByVisitorUniqueId(String uniqueId);

    List<VisitResponseDTO> getAllVisits();

    Map<String, Integer> getDashboardData();

    VisitRecord approveVisitor(Integer visitId);

    void rejectVisitor(Integer id);

    void checkOutVisitor(Integer id);
}
