package com.infy.visitormanagement.dto;

import com.infy.visitormanagement.enums.RoleType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoleDTO {
    @NotNull(message = "{role.name.required}")
    private RoleType roleName;
}
