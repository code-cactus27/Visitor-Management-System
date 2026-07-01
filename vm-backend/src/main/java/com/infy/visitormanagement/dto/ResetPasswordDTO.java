package com.infy.visitormanagement.dto;

import lombok.Data;

@Data
public class ResetPasswordDTO {
    private String email;
    private String newPassword;
}
