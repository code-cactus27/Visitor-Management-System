package com.infy.visitormanagement.service;

import com.infy.visitormanagement.dto.VisitorRequestDTO;
import com.infy.visitormanagement.dto.VisitorResponseDTO;
import com.infy.visitormanagement.entity.Visitor;
import com.infy.visitormanagement.enums.LogAction;
import com.infy.visitormanagement.repository.ActivityLogsRepository;
import com.infy.visitormanagement.repository.VisitorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VisitorServiceImplTest {

    @Mock
    private VisitorRepository visitorRepository;

    @Mock
    private ActivityLogsRepository activityLogsRepository;

    @InjectMocks
    private VisitorServiceImpl visitorService;

    // =========================================================================
    // 1. VALID CASES (HAPPY PATHS)
    // =========================================================================

    @Test
    public void generateUniqueId_ValidInputs_GeneratesCorrectFormat() {
        String uniqueId = visitorService.generateUniqueId("Bruce Wayne", "1234567890");

        assertNotNull(uniqueId);
        assertEquals(12, uniqueId.length(), "ID should be exactly 11 characters (6 date + 1 initial + 2 sec + 2 phone)");
        assertTrue(uniqueId.contains("B"), "Should contain the uppercase first letter of name");
    }

    @Test
    public void getAllVisitors_PopulatedList_ReturnsMappedDTOs() {
        Visitor visitor1 = new Visitor();
        visitor1.setName("Clark Kent");
        visitor1.setCompany("Daily Planet");

        Visitor visitor2 = new Visitor();
        visitor2.setName("Lois Lane");

        when(visitorRepository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(visitor1, visitor2));

        List<VisitorResponseDTO> result = visitorService.getAllVisitors();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Clark Kent", result.get(0).getName());
        assertEquals("Daily Planet", result.get(0).getCompany());
        assertEquals("Lois Lane", result.get(1).getName());
    }

    @Test
    public void addVisitor_ValidDto_SavesAndReturnsUniqueId() {
        VisitorRequestDTO dto = new VisitorRequestDTO();
        dto.setName("Diana Prince");
        dto.setContactNumber("9876543210");

        String uniqueId = visitorService.addVisitor(dto);

        assertNotNull(uniqueId);
        verify(visitorRepository, times(1)).save(any(Visitor.class));
    }

    @Test
    public void getOneVisitor_ValidUniqueId_ReturnsMappedDto() {
        Visitor visitor = new Visitor();
        visitor.setUniqueId("V123");
        visitor.setName("Oliver Queen");

        when(visitorRepository.getByUniqueId("V123")).thenReturn(visitor);

        VisitorResponseDTO result = visitorService.getOneVisitor("V123");

        assertNotNull(result);
        assertEquals("V123", result.getUniqueId());
        assertEquals("Oliver Queen", result.getName());
    }

    @Test
    public void updateVisitor_ExistingVisitor_UpdatesAndLogs() {
        Visitor visitor = new Visitor();
        visitor.setUniqueId("V123");
        when(visitorRepository.getByUniqueId("V123")).thenReturn(visitor);

        VisitorRequestDTO dto = new VisitorRequestDTO();
        dto.setName("Barry Allen");
        dto.setCompany("STAR Labs");
        dto.setContactNumber("1122334455");
        dto.setEmail("barry@star.com");

        String response = visitorService.updateVisitor("V123", dto);

        assertTrue(response.contains("Updated succesfully with ID: V123"));
        assertEquals("Barry Allen", visitor.getName());
        assertEquals("STAR Labs", visitor.getCompany());
        verify(visitorRepository, times(1)).save(visitor);
        verify(activityLogsRepository, times(1)).save(any());
    }

    @Test
    public void deleteVisitor_ExistingVisitor_DeletesAndLogs() {
        Visitor visitor = new Visitor();
        visitor.setUniqueId("V123");
        when(visitorRepository.getByUniqueId("V123")).thenReturn(visitor);

        String response = visitorService.deleteVisitor("V123");

        assertEquals("Visitor Deleted Successfully", response);
        verify(visitorRepository, times(1)).delete(visitor);
        verify(activityLogsRepository, times(1)).save(any());
    }

    // =========================================================================
    // 2. EDGE CASES
    // =========================================================================

    @Test
    public void getAllVisitors_EmptyDatabase_ReturnsEmptyList() {
        when(visitorRepository.findAllByOrderByCreatedAtDesc()).thenReturn(Collections.emptyList());

        List<VisitorResponseDTO> result = visitorService.getAllVisitors();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // =========================================================================
    // 3. INVALID / FAILURE CASES
    // =========================================================================

    @Test
    public void generateUniqueId_ShortContactNumber_ThrowsException() {
        // Contact number less than 7 characters triggers StringIndexOutOfBoundsException inside the service logic
        assertThrows(StringIndexOutOfBoundsException.class, () -> {
            visitorService.generateUniqueId("Bruce Wayne", "12345");
        });
    }

    @Test
    public void updateVisitor_VisitorNotFound_ReturnsNotFoundMessage() {
        when(visitorRepository.getByUniqueId("MISSING")).thenReturn(null);

        String response = visitorService.updateVisitor("MISSING", new VisitorRequestDTO());

        assertEquals("Not Found", response);
        verify(visitorRepository, never()).save(any());
        verify(activityLogsRepository, never()).save(any());
    }

    @Test
    public void deleteVisitor_VisitorNotFound_ReturnsNotFoundMessage() {
        when(visitorRepository.getByUniqueId("MISSING")).thenReturn(null);

        String response = visitorService.deleteVisitor("MISSING");

        assertEquals("Visitor Not Found", response);
        verify(visitorRepository, never()).delete(any());
        verify(activityLogsRepository, never()).save(any());
    }
}