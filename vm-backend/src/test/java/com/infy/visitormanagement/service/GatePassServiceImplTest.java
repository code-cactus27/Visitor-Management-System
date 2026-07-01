package com.infy.visitormanagement.service;

import com.infy.visitormanagement.dto.GatePassResponseDTO;
import com.infy.visitormanagement.entity.VisitRecord;
import com.infy.visitormanagement.entity.Visitor;
import com.infy.visitormanagement.enums.VisitorStatus;
import com.infy.visitormanagement.exception.ResourceNotFoundException;
import com.infy.visitormanagement.exception.VisitorManagementException;
import com.infy.visitormanagement.repository.ActivityLogsRepository;
import com.infy.visitormanagement.repository.VisitRecordRepository;
import com.infy.visitormanagement.repository.VisitorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GatePassServiceImplTest {

    @Mock
    private VisitorRepository visitorRepository;
    @Mock
    private VisitRecordRepository visitRecordRepository;
    @Mock
    private ActivityLogsRepository activityLogsRepository;

    @InjectMocks
    private GatePassServiceImpl gatePassService;

    private Visitor sampleVisitor;
    private VisitRecord sampleVisit;

    @BeforeEach
    public void setUp() {
        sampleVisitor = new Visitor();
        sampleVisitor.setVisitorId(101);
        sampleVisitor.setUniqueId("260518K206");
        sampleVisitor.setName("John Doe");
        sampleVisitor.setCompany("Infosys");
        sampleVisitor.setContactNumber("9876543210");
        sampleVisitor.setEmail("john.doe@infosys.com");
        sampleVisitor.setNotes("Business meeting");
        sampleVisitor.setCreatedAt(LocalDateTime.now());

        sampleVisit = new VisitRecord();
        sampleVisit.setVisitId(501);
        sampleVisit.setReasonForVisit("Client Interview");
        sampleVisit.setVisitDate(LocalDate.now());
        sampleVisit.setExpectedTime(LocalTime.now());
        sampleVisit.setPassDuration(4);
        sampleVisit.setEntryTime(LocalDateTime.now());
        sampleVisit.setExitTime(LocalDateTime.now().plusHours(4));
        sampleVisit.setPassExpiry(LocalDateTime.now().plusHours(4));
        sampleVisit.setStatusOnTime(VisitorStatus.CHECKED_IN);
        sampleVisit.setVisitor(sampleVisitor);
    }

    // =========================================================================
    // 1. VALID CASES (HAPPY PATHS)
    // =========================================================================

    @Test
    public void buildGatePass_ValidData_ReturnsFullGatePassResponseDTO() throws VisitorManagementException {
        // Arrange
        when(visitorRepository.findById(101)).thenReturn(Optional.of(sampleVisitor));
        when(visitRecordRepository.findById(501)).thenReturn(Optional.of(sampleVisit));

        // Act
        GatePassResponseDTO dto = gatePassService.buildGatePass(101, 501);

        // Assert
        assertNotNull(dto, "The generated GatePass DTO should not be null");

        // Assert Visitor field mapping accuracy
        assertEquals(sampleVisitor.getVisitorId(), dto.getVisitorId());
        assertEquals(sampleVisitor.getUniqueId(), dto.getUniqueId());
        assertEquals(sampleVisitor.getName(), dto.getName());
        assertEquals(sampleVisitor.getCompany(), dto.getCompany());
        assertEquals(sampleVisitor.getContactNumber(), dto.getContactNumber());
        assertEquals(sampleVisitor.getEmail(), dto.getEmail());
        assertEquals(sampleVisitor.getNotes(), dto.getNotes());
        assertEquals(sampleVisitor.getCreatedAt(), dto.getCreatedAt());

        // Assert Visit record field mapping accuracy
        assertEquals(sampleVisit.getVisitId(), dto.getVisitId());
        assertEquals(sampleVisit.getReasonForVisit(), dto.getReasonForVisit());
        assertEquals(sampleVisit.getVisitDate(), dto.getVisitDate());
        assertEquals(sampleVisit.getExpectedTime(), dto.getExpectedTime());
        assertEquals(sampleVisit.getPassDuration(), dto.getPassDuration());
        assertEquals(sampleVisit.getEntryTime(), dto.getEntryTime());
        assertEquals(sampleVisit.getExitTime(), dto.getExitTime());
        assertEquals(sampleVisit.getPassExpiry(), dto.getPassExpiry());
        assertEquals("CHECKED_IN", dto.getStatus());

        // Verify activity was captured
        verify(activityLogsRepository, times(1)).save(any());
    }

    // =========================================================================
    // 2. EDGE CASES (MINIMAL / NULL DATA EVALUATIONS)
    // =========================================================================

    @Test
    public void buildGatePass_NullStatusInVisit_DefaultsToPending() throws VisitorManagementException {
        // Arrange: Explicitly trigger the alternative branch of your conditional ternary statement
        sampleVisit.setStatusOnTime(null);

        when(visitorRepository.findById(101)).thenReturn(Optional.of(sampleVisitor));
        when(visitRecordRepository.findById(501)).thenReturn(Optional.of(sampleVisit));

        // Act
        GatePassResponseDTO dto = gatePassService.buildGatePass(101, 501);

        // Assert
        assertNotNull(dto);
        assertEquals("PENDING", dto.getStatus(), "Status should default to PENDING when database state is null");
        verify(activityLogsRepository, times(1)).save(any());
    }

    @Test
    public void buildGatePass_MinimalDataWithNullFields_MapsSuccessfully() throws VisitorManagementException {
        // Arrange: Check flexibility of parsing framework under non-required null inputs
        sampleVisitor.setNotes(null);
        sampleVisitor.setEmail(null);
        sampleVisit.setExitTime(null);

        when(visitorRepository.findById(101)).thenReturn(Optional.of(sampleVisitor));
        when(visitRecordRepository.findById(501)).thenReturn(Optional.of(sampleVisit));

        // Act
        GatePassResponseDTO dto = gatePassService.buildGatePass(101, 501);

        // Assert
        assertNotNull(dto);
        assertNull(dto.getNotes());
        assertNull(dto.getEmail());
        assertNull(dto.getExitTime());
        verify(activityLogsRepository, times(1)).save(any());
    }

    // =========================================================================
    // 3. INVALID / FAILURE CASES (EXCEPTIONS & REJECTIONS)
    // =========================================================================

    @Test
    public void buildGatePass_VisitorNotFound_ThrowsResourceNotFoundException() {
        // Arrange
        when(visitorRepository.findById(101)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            gatePassService.buildGatePass(101, 501);
        });

        assertEquals("GatePassService.VISITOR_NOT_FOUND", exception.getMessage());
        verify(visitRecordRepository, never()).findById(anyInt());
        verify(activityLogsRepository, never()).save(any());
    }

    @Test
    public void buildGatePass_VisitNotFound_ThrowsResourceNotFoundException() {
        // Arrange
        when(visitorRepository.findById(101)).thenReturn(Optional.of(sampleVisitor));
        when(visitRecordRepository.findById(501)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            gatePassService.buildGatePass(101, 501);
        });

        assertEquals("GatePassService.VISIT_NOT_FOUND", exception.getMessage());
        verify(activityLogsRepository, never()).save(any());
    }

    @Test
    public void buildGatePass_VisitorVisitMismatch_ThrowsVisitorManagementException() {
        // Arrange: Make visitor ID belong to someone else entirely to trigger validation failure
        Visitor rogueVisitor = new Visitor();
        rogueVisitor.setVisitorId(999);
        sampleVisit.setVisitor(rogueVisitor);

        when(visitorRepository.findById(101)).thenReturn(Optional.of(sampleVisitor));
        when(visitRecordRepository.findById(501)).thenReturn(Optional.of(sampleVisit));

        // Act & Assert
        VisitorManagementException exception = assertThrows(VisitorManagementException.class, () -> {
            gatePassService.buildGatePass(101, 501);
        });

        assertEquals("GatePassService.VISIT_VISITOR_MISMATCH", exception.getMessage());
        verify(activityLogsRepository, never()).save(any());
    }

    @Test
    public void buildGatePass_ActivityLogSavingFailure_PropagatesException() {
        // Arrange: Valid credentials but intermediate logging pipeline breaks down
        when(visitorRepository.findById(101)).thenReturn(Optional.of(sampleVisitor));
        when(visitRecordRepository.findById(501)).thenReturn(Optional.of(sampleVisit));
        when(activityLogsRepository.save(any())).thenThrow(new RuntimeException("Audit disk volume full"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            gatePassService.buildGatePass(101, 501);
        });

        assertEquals("Audit disk volume full", exception.getMessage());
    }
}