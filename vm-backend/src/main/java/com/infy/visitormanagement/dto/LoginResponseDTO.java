package com.infy.visitormanagement.dto;

import com.infy.visitormanagement.enums.RoleType;
import lombok.Data;

@Data
public class LoginResponseDTO {
    private String Email;
    private RoleType role;
}