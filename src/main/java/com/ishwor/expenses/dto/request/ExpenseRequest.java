package com.ishwor.expenses.dto.request;


import jakarta.validation.constraints.NotBlank;

public record ExpenseRequest(
        @NotBlank(message = "expense name is required") String name,
        String description) {
}