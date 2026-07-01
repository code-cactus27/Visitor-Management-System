package com.infy.visitormanagement.service;

import java.util.List;

import com.infy.visitormanagement.dto.VisitorResponseDTO;
import com.infy.visitormanagement.dto.VisitorRequestDTO;

public interface VisitorService {
    List<VisitorResponseDTO> getAllVisitors();

    String addVisitor(VisitorRequestDTO visitorRequestDto);

    VisitorResponseDTO getOneVisitor(String uniqueId);

    String updateVisitor(String uniqueId, VisitorRequestDTO dto);

    String generateUniqueId(String name, String contactNumber);

    String deleteVisitor(String uniqueId);
}
