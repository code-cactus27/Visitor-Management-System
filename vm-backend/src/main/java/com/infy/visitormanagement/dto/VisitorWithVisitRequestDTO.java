package com.infy.visitormanagement.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VisitorWithVisitRequestDTO {
    @NotNull(message = "Name is required")
    @Size(max = 100)
    private String name;
    @Size(max = 100)
    private String company;
    @NotNull(message = "Contact Number is required")
    @Pattern(regexp = "[6-9][0-9]{9}", message = "Invalid Contact Number")
    private String contactNumber;
    @Email(message = "Invalid Email Format")
    private String email;
    @Size(max = 255)
    private String notes;
    @NotNull(message = "Reason for visit is required")
    private String reasonForVisit;
    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Minimum value required is 1")
    private Integer passDuration;
    private LocalDate visitDate;
    private LocalTime expectedTime;
}
