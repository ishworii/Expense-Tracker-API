package com.ishwor.expenses.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

public record ExpenseResponse(
        Long id,
        BigDecimal amount,
        String description,
        Long categoryId,
        String categoryName,
        Long userId,
        String userName,
        Instant createdAt
) {}
