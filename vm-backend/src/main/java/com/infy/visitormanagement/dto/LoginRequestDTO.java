package com.infy.visitormanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {
    @Email(message = "{email.invalid}")
    @NotBlank(message = "{email.required}")
    private String email;
    @NotBlank(message = "{password.required}")
    private String password;
}
