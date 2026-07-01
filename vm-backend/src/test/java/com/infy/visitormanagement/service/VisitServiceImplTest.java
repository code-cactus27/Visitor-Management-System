package com.infy.visitormanagement.service;

import com.infy.visitormanagement.dto.VisitRequestDTO;
import com.infy.visitormanagement.dto.VisitResponseDTO;
import com.infy.visitormanagement.dto.VisitorWithVisitRequestDTO;
import com.infy.visitormanagement.entity.VisitRecord;
import com.infy.visitormanagement.entity.Visitor;
import com.infy.visitormanagement.enums.VisitorStatus;
import com.infy.visitormanagement.repository.ActivityLogsRepository;
import com.infy.visitormanagement.repository.VisitRecordRepository;
import com.infy.visitormanagement.repository.VisitorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VisitServiceImplTest {

    @Mock
    private VisitorRepository visitorRepository;
    @Mock
    private VisitRecordRepository visitRecordRepository;
    @Mock
    private VisitorService visitorService;
    @Mock
    private ActivityLogsRepository activityLogsRepository;

    @InjectMocks
    private VisitServiceImpl visitService;

    // =========================================================================
    // 1. VALID CASES (HAPPY PATHS)
    // =========================================================================

    @Test
    public void addVisitNew_ValidData_SavesVisitorAndVisit() {
        VisitorWithVisitRequestDTO dto = new VisitorWithVisitRequestDTO();
        dto.setName("Arthur Curry");
        dto.setContactNumber("7778889999");
        dto.setReasonForVisit("Business");
        dto.setPassDuration(3);
        dto.setVisitDate(LocalDate.now());
        dto.setExpectedTime(LocalTime.now());

        when(visitorService.generateUniqueId(anyString(), anyString())).thenReturn("AQUAMAN1");

        String response = visitService.addVisitNew(dto);

        assertNotNull(response);
        assertTrue(response.contains("AQUAMAN1"));
        verify(visitorRepository, times(1)).save(any(Visitor.class));
    }

    @Test
    public void addVisitExisting_ValidData_SavesVisitRecord() {
        Visitor visitor = new Visitor();
        visitor.setUniqueId("V999");
        when(visitorRepository.getByUniqueId("V999")).thenReturn(visitor);

        VisitRequestDTO dto = new VisitRequestDTO();
        dto.setUniqueId("V999");
        dto.setVisitDate(LocalDate.now());
        dto.setExpectedTime(LocalTime.now());
        dto.setPassDuration(2);

        String response = visitService.addVisitExisting(dto);

        assertTrue(response.contains("Visit added for existing visitor: V999"));
        verify(visitRecordRepository, times(1)).save(any(VisitRecord.class));
    }

    @Test
    public void getVisitsByVisitorUniqueId_PopulatedData_ReturnsVisits() {
        Visitor visitor = new Visitor();
        visitor.setVisitorId(55);
        visitor.setUniqueId("V55");

        VisitRecord visit = new VisitRecord();
        visit.setVisitor(visitor);
        visit.setStatusOnTime(VisitorStatus.PENDING);

        when(visitRecordRepository.findByVisitor_UniqueIdOrderByVisitDateDescExpectedTimeDesc("V55"))
                .thenReturn(List.of(visit));

        List<VisitResponseDTO> result = visitService.getVisitsByVisitorUniqueId("V55");

        assertFalse(result.isEmpty());
        assertEquals(55, result.get(0).getVisitorId());
    }

    @Test
    public void getDashboardData_ReturnsCorrectMetricMap() {
        when(visitRecordRepository.countTotalVisitors()).thenReturn(100);
        when(visitRecordRepository.countTodayVisitors()).thenReturn(20);
        when(visitRecordRepository.countCheckedIn()).thenReturn(15);
        when(visitRecordRepository.countCheckedOut()).thenReturn(60);
        when(visitRecordRepository.countPending()).thenReturn(2);
        when(visitRecordRepository.countExpired()).thenReturn(3);

        Map<String, Integer> metrics = visitService.getDashboardData();

        assertEquals(100, metrics.get("totalVisitors"));
        assertEquals(20, metrics.get("expectedToday"));
        assertEquals(15, metrics.get("checkedIn"));
        assertEquals(60, metrics.get("checkedOut"));
        assertEquals(2, metrics.get("pending"));
        assertEquals(3, metrics.get("expired"));
    }

    @Test
    public void approveVisitor_ExistingRecord_UpdatesToCheckedIn() {
        Visitor visitor = new Visitor();
        VisitRecord visit = new VisitRecord();
        visit.setVisitor(visitor);
        visit.setPassDuration(4);

        when(visitRecordRepository.findById(10)).thenReturn(Optional.of(visit));
        when(visitRecordRepository.save(any(VisitRecord.class))).thenAnswer(i -> i.getArguments()[0]);

        VisitRecord result = visitService.approveVisitor(10);

        assertEquals(VisitorStatus.CHECKED_IN, result.getStatusOnTime());
        assertNotNull(result.getEntryTime());
        verify(activityLogsRepository, times(1)).save(any());
    }

    @Test
    public void rejectVisitor_ExistingRecord_UpdatesToExpired() {
        Visitor visitor = new Visitor();
        VisitRecord visit = new VisitRecord();
        visit.setVisitor(visitor);

        when(visitRecordRepository.findById(11)).thenReturn(Optional.of(visit));

        assertDoesNotThrow(() -> visitService.rejectVisitor(11));
        assertEquals(VisitorStatus.EXPIRED, visit.getStatusOnTime());
        assertNull(visit.getEntryTime());
    }

    @Test
    public void checkOutVisitor_ExistingRecord_UpdatesToCheckedOut() {
        Visitor visitor = new Visitor();
        VisitRecord visit = new VisitRecord();
        visit.setVisitor(visitor);

        when(visitRecordRepository.findById(12)).thenReturn(Optional.of(visit));

        assertDoesNotThrow(() -> visitService.checkOutVisitor(12));
        assertEquals(VisitorStatus.CHECKED_OUT, visit.getStatusOnTime());
        assertNotNull(visit.getExitTime());
    }

    // =========================================================================
    // 2. EDGE CASES (STATE ENGINE TRANSITIONS & SORTING MATRIX)
    // =========================================================================

    @Test
    public void getAllVisits_StaleRecords_TriggersAutoTransitionsAndExecutesAllSortingBranches() {
        Visitor visitor = new Visitor();
        visitor.setVisitorId(1);

        // 1. Checked-in pass where checkout threshold is past -> triggers auto-checkout
        VisitRecord r1 = new VisitRecord();
        r1.setStatusOnTime(VisitorStatus.CHECKED_IN);
        r1.setExitTime(LocalDateTime.now().minusHours(1));
        r1.setVisitDate(LocalDate.now());
        r1.setExpectedTime(LocalTime.NOON);
        r1.setVisitor(visitor);

        // 2. Pending pass where validity expiry threshold is past -> triggers auto-expiry
        VisitRecord r2 = new VisitRecord();
        r2.setStatusOnTime(VisitorStatus.PENDING);
        r2.setPassExpiry(LocalDateTime.now().minusMinutes(5));
        r2.setVisitDate(LocalDate.now());
        r2.setExpectedTime(LocalTime.NOON);
        r2.setVisitor(visitor);

        // 3. Record with matching priority status but EARLIER date to verify second tier sorting
        VisitRecord r3 = new VisitRecord();
        r3.setStatusOnTime(VisitorStatus.PENDING);
        r3.setPassExpiry(LocalDateTime.now().plusHours(5));
        r3.setVisitDate(LocalDate.now().minusDays(2));
        r3.setExpectedTime(LocalTime.NOON);
        r3.setVisitor(visitor);

        // 4. Record with matching priority AND date, but EARLIER expected time to verify final tier sorting
        VisitRecord r4 = new VisitRecord();
        r4.setStatusOnTime(VisitorStatus.PENDING);
        r4.setPassExpiry(LocalDateTime.now().plusHours(5));
        r4.setVisitDate(LocalDate.now().minusDays(2));
        r4.setExpectedTime(LocalTime.MIDNIGHT);
        r4.setVisitor(visitor);

        List<VisitRecord> mixedRecords = new ArrayList<>(List.of(r1, r2, r3, r4));
        when(visitRecordRepository.findAll()).thenReturn(mixedRecords);

        List<VisitResponseDTO> sortedResult = visitService.getAllVisits();

        assertNotNull(sortedResult);
        // Confirm dynamic lifecycle modification blocks fired safely
        assertEquals(VisitorStatus.CHECKED_OUT.name(), r1.getStatusOnTime().name());
        assertEquals(VisitorStatus.EXPIRED.name(), r2.getStatusOnTime().name());

        // Confirm sorting ranking structure executed correctly
        assertEquals(r4.getExpectedTime(), sortedResult.get(0).getExpectedTime(), "Earliest expected time goes first when priority and date match");
    }

    // =========================================================================
    // 3. INVALID / FAILURE CASES (REJECTION WRAPPERS)
    // =========================================================================

    @Test
    public void approveVisitor_NotFound_ThrowsRuntimeException() {
        when(visitRecordRepository.findById(404)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            visitService.approveVisitor(404);
        });
        assertEquals("Visitor not found", exception.getMessage());
    }

    @Test
    public void rejectVisitor_NotFound_ThrowsRuntimeException() {
        when(visitRecordRepository.findById(404)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            visitService.rejectVisitor(404);
        });
        assertEquals("Visit not found", exception.getMessage());
    }

    @Test
    public void checkOutVisitor_NotFound_ThrowsRuntimeException() {
        when(visitRecordRepository.findById(404)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            visitService.checkOutVisitor(404);
        });
        assertEquals("Visit not found", exception.getMessage());
    }
}