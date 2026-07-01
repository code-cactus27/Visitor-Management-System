package com.infy.visitormanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.infy.visitormanagement.dto.VisitorRequestDTO;
import com.infy.visitormanagement.dto.VisitorResponseDTO;
import com.infy.visitormanagement.service.VisitorService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/gate")
public class VisitorController {
    @Autowired
    private VisitorService visitorService;

    @PostMapping("/add")
    public ResponseEntity<String> addVisitor(@Valid @RequestBody VisitorRequestDTO dto) {
        String successMessage = visitorService.addVisitor(dto);
        return new ResponseEntity<String>(successMessage, HttpStatus.CREATED);
    }

    @GetMapping("/visitors")
    public ResponseEntity<List<VisitorResponseDTO>> getVisitors() {
        return new ResponseEntity<>(visitorService.getAllVisitors(), HttpStatus.OK);
    }

    @GetMapping("/visitor/{unique_id}")
    public ResponseEntity<VisitorResponseDTO> getOneVisitor(@PathVariable String unique_id) {
        VisitorResponseDTO message = visitorService.getOneVisitor(unique_id);
        return new ResponseEntity<VisitorResponseDTO>(message, HttpStatus.OK);
    }

    @PutMapping("/visitor/{uniqueId}")
    public ResponseEntity<String> updateVisitor(@PathVariable String uniqueId, @RequestBody VisitorRequestDTO dto) {
        String updated = visitorService.updateVisitor(uniqueId, dto);
        return new ResponseEntity<String>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/visitor/{uniqueId}")
    public ResponseEntity<String> deleteVisitor(@PathVariable String uniqueId) {
        String result = visitorService.deleteVisitor(uniqueId);
        return ResponseEntity.ok(result);
    }
}
