package com.infy.visitormanagement.service;

import com.infy.visitormanagement.dto.ActivityLogsDTO;
import com.infy.visitormanagement.entity.ActivityLogs;
import com.infy.visitormanagement.enums.LogAction;
import com.infy.visitormanagement.repository.ActivityLogsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActivityLogsServiceTest {

    @Mock
    private ActivityLogsRepository activityLogsRepository;

    @InjectMocks
    private ActivityLogsService activityLogsService;

    // =========================================================================
    // 1. VALID CASES (HAPPY PATHS)
    // =========================================================================

    @Test
    public void getActivityLogs_ValidPopulatedList_ReturnsMappedDTOs() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        ActivityLogs log1 = new ActivityLogs("V101", LogAction.CHECKED_IN, "Checked In Successfully", now);
        ActivityLogs log2 = new ActivityLogs("V102", LogAction.CHECKED_OUT, "Checked Out Successfully", now.minusHours(1));

        when(activityLogsRepository.findAllByOrderByTimestampDesc()).thenReturn(List.of(log1, log2));

        // Act
        List<ActivityLogsDTO> result = activityLogsService.getActivityLogs();

        // Assert
        assertNotNull(result, "The returned list should not be null");
        assertEquals(2, result.size(), "The list size should match the repository output");

        // Deep verification of ModelMapper conversion
        assertEquals("V101", result.get(0).getUniqueId());
        assertEquals(LogAction.CHECKED_IN, result.get(0).getAction());
        assertEquals("Checked In Successfully", result.get(0).getMessage());
        assertEquals(now, result.get(0).getTimestamp());

        assertEquals("V102", result.get(1).getUniqueId());
        verify(activityLogsRepository, times(1)).findAllByOrderByTimestampDesc();
    }

    // =========================================================================
    // 2. EDGE CASES (UNUSUAL OR EMPTY DATA STATES)
    // =========================================================================

    @Test
    public void getActivityLogs_EmptyDatabase_ReturnsEmptyList() {
        // Arrange
        when(activityLogsRepository.findAllByOrderByTimestampDesc()).thenReturn(Collections.emptyList());

        // Act
        List<ActivityLogsDTO> result = activityLogsService.getActivityLogs();

        // Assert
        assertNotNull(result, "Should return an empty list object, not null");
        assertTrue(result.isEmpty(), "Resulting list should be completely empty");
        verify(activityLogsRepository, times(1)).findAllByOrderByTimestampDesc();
    }

    @Test
    public void getActivityLogs_NullFieldsInEntity_MapsWithoutCrashing() {
        // Arrange: Entity has null values for some properties
        ActivityLogs incompleteLog = new ActivityLogs();
        incompleteLog.setUniqueId("V999");
        incompleteLog.setAction(null);
        incompleteLog.setMessage(null);
        incompleteLog.setTimestamp(null);

        when(activityLogsRepository.findAllByOrderByTimestampDesc()).thenReturn(List.of(incompleteLog));

        // Act & Assert
        // Ensures ModelMapper doesn't throw a NullPointerException during iteration
        List<ActivityLogsDTO> result = assertDoesNotThrow(() -> activityLogsService.getActivityLogs());

        assertEquals(1, result.size());
        assertEquals("V999", result.get(0).getUniqueId());
        assertNull(result.get(0).getAction());
        assertNull(result.get(0).getMessage());
        assertNull(result.get(0).getTimestamp());
    }

    // =========================================================================
    // 3. INVALID / FAILURE CASES (INFRASTRUCTURE & RUNTIME EXCEPTIONS)
    // =========================================================================

    @Test
    public void getActivityLogs_DatabaseFailure_PropagatesException() {
        // Arrange: Simulating a database failure or connection timeout
        when(activityLogsRepository.findAllByOrderByTimestampDesc())
                .thenThrow(new RuntimeException("Database connection timeout error"));

        // Act & Assert
        // Ensures that errors from the repository layer are safely propagated up the stack
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            activityLogsService.getActivityLogs();
        });

        assertEquals("Database connection timeout error", exception.getMessage());
        verify(activityLogsRepository, times(1)).findAllByOrderByTimestampDesc();
    }
}