package com.ishwor.expenses.dto.request;


import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
        @NotBlank(message = "category name is required") String name,
        String description) {
}