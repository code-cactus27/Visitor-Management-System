package com.infy.visitormanagement.dto;

import lombok.Data;

@Data
public class ForgetPasswordRequestDTO {
    private String email;
    private String last4Digits;
}
