package com.infy.visitormanagement.service;

import com.infy.visitormanagement.dto.GatePassResponseDTO;
import com.infy.visitormanagement.exception.VisitorManagementException;

public interface GatePassService {
    GatePassResponseDTO buildGatePass(Integer visitorId, Integer visitId) throws VisitorManagementException;
}
