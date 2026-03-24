package com.sboo.todoapi.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TodoResponse(
        long id,
        String title,
        String description,
        boolean completed,
        LocalDateTime createdAt,
        LocalDate dueDate
){}
