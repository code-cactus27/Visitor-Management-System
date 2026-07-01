package com.infy.visitormanagement.service;

import com.infy.visitormanagement.dto.FeedbackDTO;
import com.infy.visitormanagement.entity.Feedback;
import com.infy.visitormanagement.repository.FeedbackRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FeedbackServiceImplTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @InjectMocks
    private FeedbackServiceImpl feedbackService;

    // =========================================================================
    // 1. VALID CASES (HAPPY PATHS)
    // =========================================================================

    @Test
    public void saveFeedback_ValidData_SavesSuccessfully() {
        // Arrange
        FeedbackDTO dto = new FeedbackDTO();
        dto.setVisitorId("12");
        dto.setVisitorName("John Doe");
        dto.setFeedbackText("Smooth check-in process.");
        dto.setRating(5);

        ArgumentCaptor<Feedback> feedbackCaptor = ArgumentCaptor.forClass(Feedback.class);

        // Act
        String response = feedbackService.saveFeedback(dto);

        // Assert
        assertEquals("Feedback saved successfully!", response);
        verify(feedbackRepository, times(1)).save(feedbackCaptor.capture());

        Feedback savedFeedback = feedbackCaptor.getValue();
        assertEquals("12", savedFeedback.getVisitorId());
        assertEquals("John Doe", savedFeedback.getVisitorName());
        assertEquals("Smooth check-in process.", savedFeedback.getFeedbackText());
        assertEquals(5, savedFeedback.getRating());
        assertNotNull(savedFeedback.getCreatedAt(), "Timestamp should be generated automatically");
    }

    @Test
    public void getAllFeedbacks_PopulatedData_ReturnsList() {
        // Arrange
        Feedback feedback1 = new Feedback();
        feedback1.setFeedbackText("Good experience");
        Feedback feedback2 = new Feedback();
        feedback2.setFeedbackText("Excellent interface");

        when(feedbackRepository.findAll()).thenReturn(List.of(feedback1, feedback2));

        // Act
        List<Feedback> result = feedbackService.getAllFeedbacks();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Good experience", result.get(0).getFeedbackText());
        assertEquals("Excellent interface", result.get(1).getFeedbackText());
        verify(feedbackRepository, times(1)).findAll();
    }

    // =========================================================================
    // 2. EDGE CASES (MINIMAL OR EMPTY STATES)
    // =========================================================================

    @Test
    public void saveFeedback_NullFieldsInDto_SavesWithoutCrashing() {
        // Arrange: Incomplete DTO with missing text, name, and zero rating
        FeedbackDTO minimalDto = new FeedbackDTO();
        minimalDto.setVisitorId("99");
        minimalDto.setVisitorName(null);
        minimalDto.setFeedbackText(null);
        minimalDto.setRating(0);

        ArgumentCaptor<Feedback> feedbackCaptor = ArgumentCaptor.forClass(Feedback.class);

        // Act & Assert
        // Ensures the direct mapping methods inside the service don't trigger NullPointerException
        String response = assertDoesNotThrow(() -> feedbackService.saveFeedback(minimalDto));

        assertEquals("Feedback saved successfully!", response);
        verify(feedbackRepository, times(1)).save(feedbackCaptor.capture());

        Feedback savedFeedback = feedbackCaptor.getValue();
        assertEquals("99", savedFeedback.getVisitorId());
        assertNull(savedFeedback.getVisitorName());
        assertNull(savedFeedback.getFeedbackText());
        assertEquals(0, savedFeedback.getRating());
        assertNotNull(savedFeedback.getCreatedAt());
    }

    @Test
    public void getAllFeedbacks_NoDataInRepository_ReturnsEmptyList() {
        // Arrange
        when(feedbackRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Feedback> result = feedbackService.getAllFeedbacks();

        // Assert
        assertNotNull(result, "Service should return an empty list object, not null");
        assertTrue(result.isEmpty(), "Resulting list must be completely empty");
        verify(feedbackRepository, times(1)).findAll();
    }

    // =========================================================================
    // 3. INVALID / FAILURE CASES (INFRASTRUCTURE BREAKAGES)
    // =========================================================================

    @Test
    public void saveFeedback_DatabaseFailure_PropagatesException() {
        // Arrange
        FeedbackDTO dto = new FeedbackDTO();
        dto.setVisitorId("12");

        when(feedbackRepository.save(any(Feedback.class)))
                .thenThrow(new RuntimeException("SQL Transient Data Access Resource Failure"));

        // Act & Assert
        // Assures that transaction or repository level faults leak up safely to be caught by your Centralized Exception Handler
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            feedbackService.saveFeedback(dto);
        });

        assertEquals("SQL Transient Data Access Resource Failure", exception.getMessage());
        verify(feedbackRepository, times(1)).save(any(Feedback.class));
    }

    @Test
    public void getAllFeedbacks_DatabaseFailure_PropagatesException() {
        // Arrange
        when(feedbackRepository.findAll()).thenThrow(new RuntimeException("Database connection closed"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            feedbackService.getAllFeedbacks();
        });

        assertEquals("Database connection closed", exception.getMessage());
        verify(feedbackRepository, times(1)).findAll();
    }
}