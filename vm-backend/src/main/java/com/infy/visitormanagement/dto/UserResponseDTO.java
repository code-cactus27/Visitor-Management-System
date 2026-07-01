package com.infy.visitormanagement.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserResponseDTO {
    private Integer userId;
    private String name;
    private String email;
    private String phone;
    private String roleName;
    private LocalDateTime createdAt;
}
