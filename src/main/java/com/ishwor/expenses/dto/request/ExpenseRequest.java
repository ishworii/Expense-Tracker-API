package com.ishwor.expenses.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseRequest(
        @NotNull(message = "Amount is required")
        @Min(value = 1, message = "Amount must be greater than zero")
        BigDecimal amount,

        @NotBlank(message = "Description cannot be empty")
        String description,

        @NotNull(message = "Category ID is required")
        Long categoryId,

        @NotNull(message = "User ID is required")
        Long userId,

        LocalDate expenseDate
) {}
