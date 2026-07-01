package com.infy.visitormanagement.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO {
    private Integer userId;
    @NotBlank(message = "{name.required}")
    @Size(min = 3, max = 50, message = "{name.size}")
    private String name;
    @NotBlank(message = "{email.required}")
    @Email(message = "{email.invalid}")
    private String email;
    @NotBlank(message = "{phone.required}")
    private String phone;
    @NotBlank(message = "{password.required}")
    @Size(min = 6, message = "{password.size}")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&*+=])(?=\\S+$).*$",
            message = "{password.pattern}")
    private String password;
    @NotNull(message = "{role.required}")
    @Valid
    private RoleDTO role;
}
