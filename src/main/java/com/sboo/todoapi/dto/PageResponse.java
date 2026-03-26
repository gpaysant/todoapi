package com.sboo.todoapi.dto;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        int totalElements,
        int totalPages,
        int currentPage,
        int pageSize,
        boolean isLast
) {}
