package com.github_manager.controller.role.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class RoleCreationRequest {
    @NotBlank
    private String name;
}