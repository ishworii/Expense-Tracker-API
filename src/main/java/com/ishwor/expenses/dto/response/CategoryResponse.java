package com.ishwor.expenses.dto.response;

import java.time.Instant;

public record CategoryResponse(
        Long id,
        String name,
        String description,
        Instant createdAt) {
}