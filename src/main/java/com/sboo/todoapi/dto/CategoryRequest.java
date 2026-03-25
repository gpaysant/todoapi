package com.sboo.todoapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequest (
        @NotBlank String name,
        @Size(max = 100) String description
) {}
