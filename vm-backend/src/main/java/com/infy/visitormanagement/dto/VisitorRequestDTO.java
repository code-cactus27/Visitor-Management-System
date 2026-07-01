package com.infy.visitormanagement.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VisitorRequestDTO {
    private Integer visitorId;
    private String uniqueId;
    @NotNull(message = "Name is Required")
    @Size(max = 100)
    private String name;
    @Size(max = 100)
    private String company;
    @NotNull(message = "Contact Number is Required")
    @Pattern(regexp = "[6-9][0-9]{9}", message = "Invalid Contact Number")
    private String contactNumber;
    @Email(message = "Invalid Email Format")
    @Pattern(regexp = "[A-Za-z0-9]+@[A-Za-z0-9].[A-Za-z]", message = "Invalid Format")
    private String email;
    @Size(max = 255)
    private String notes;
    private LocalDateTime createdAt;
}
