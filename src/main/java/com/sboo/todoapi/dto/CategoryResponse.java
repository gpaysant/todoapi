package com.sboo.todoapi.dto;

public record CategoryResponse(
        long id,
        String name,
        String description,
        int todoCount
) {}
