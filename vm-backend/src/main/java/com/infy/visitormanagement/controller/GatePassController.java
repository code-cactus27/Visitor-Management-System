package com.infy.visitormanagement.controller;
import com.infy.visitormanagement.dto.GatePassResponseDTO;
import com.infy.visitormanagement.exception.VisitorManagementException;
import com.infy.visitormanagement.service.GatePassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gate")
public class GatePassController {
    @Autowired
    private GatePassService gatePassService;

    @GetMapping("/gatepass/{visitorId}/visit/{visitId}")
    public ResponseEntity<GatePassResponseDTO> getGatePass(
            @PathVariable Integer visitorId,
            @PathVariable Integer visitId) throws VisitorManagementException {
        GatePassResponseDTO dto = gatePassService.buildGatePass(visitorId, visitId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
