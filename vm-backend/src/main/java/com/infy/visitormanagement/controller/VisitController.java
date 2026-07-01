package com.infy.visitormanagement.controller;
import com.infy.visitormanagement.dto.VisitRequestDTO;
import com.infy.visitormanagement.dto.VisitResponseDTO;
import com.infy.visitormanagement.dto.VisitorWithVisitRequestDTO;
import com.infy.visitormanagement.service.VisitService;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/gate")
public class VisitController {
    @Autowired
    private VisitService visitService;
    @PostMapping("/visit/new")
    public ResponseEntity<String> addVisitNew(@Valid @RequestBody VisitorWithVisitRequestDTO dto) {
        return new ResponseEntity<>(visitService.addVisitNew(dto), HttpStatus.CREATED);
    }
    @PostMapping("/visit/existing")
    public ResponseEntity<String> addVisitExisting(@Valid @RequestBody VisitRequestDTO dto) {
                System.out.println("Unique Id = "+dto.getUniqueId());
        return new ResponseEntity<>(visitService.addVisitExisting(dto), HttpStatus.CREATED);
    }
    @GetMapping("/visit/visitor/{uniqueId}")
    public ResponseEntity<List<VisitResponseDTO>> getVisitsByVisitor(@PathVariable String uniqueId) {
        return new ResponseEntity<>(visitService.getVisitsByVisitorUniqueId(uniqueId), HttpStatus.OK);
    }
    @GetMapping("/visit/all")
    public ResponseEntity<List<VisitResponseDTO>> getAllVisits() {
        return new ResponseEntity<>(visitService.getAllVisits(), HttpStatus.OK);
    }
    @GetMapping("/dashboard")
    public Map<String,Integer> getDashBoard(){
                return visitService.getDashboardData();
    }
    @PutMapping("/approve/{uniqueId}")
    public ResponseEntity<?> approveVisitor(@PathVariable Integer uniqueId){
                return ResponseEntity.ok(visitService.approveVisitor(uniqueId));
    }
    @PutMapping("/reject/{id}")
    public ResponseEntity<String> rejectVisitor(@PathVariable Integer id){
                visitService.rejectVisitor(id);
                return ResponseEntity.ok("Visitor rejected successfully");
    }
    @PutMapping("/checkout/{id}")
    public ResponseEntity<Map<String,String>> checkOutVisitor(@PathVariable Integer id){
                visitService.checkOutVisitor(id);
                Map<String,String> response=new HashMap<>();
                response.put("message", "visitor checked out successfully");
                return ResponseEntity.ok(response);
    }
}
