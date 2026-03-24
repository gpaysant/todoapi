package com.sboo.todoapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record TodoRequest(
        @NotBlank String title,
        @Size(max = 100) String description,
        Boolean completed,
        LocalDate dueDate
) {}
