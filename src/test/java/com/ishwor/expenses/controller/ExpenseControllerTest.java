package com.ishwor.expenses.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ishwor.expenses.dto.request.ExpenseRequest;
import com.ishwor.expenses.dto.response.ExpenseResponse;
import com.ishwor.expenses.exception.ResourceNotFoundException;
import com.ishwor.expenses.service.ExpenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExpenseService expenseService;

    @Autowired
    private ObjectMapper objectMapper;

    private ExpenseRequest expenseRequest;
    private ExpenseResponse expenseResponse;

    @BeforeEach
    void setUp() {
        expenseRequest = new ExpenseRequest(
                BigDecimal.valueOf(100.50),
                "Monthly groceries",
                1L,
                1L,
                LocalDate.now()
        );

        expenseResponse = new ExpenseResponse(
                1L,
                BigDecimal.valueOf(100.50),
                "Monthly groceries",
                1L,
                "Groceries",
                1L,
                "John Doe",
                Instant.now()
        );
    }

    @Test
    void testAddExpense() throws Exception {
        when(expenseService.addExpense(any(ExpenseRequest.class))).thenReturn(expenseResponse);

        mockMvc.perform(post("/api/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expenseRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(100.50))
                .andExpect(jsonPath("$.description").value("Monthly groceries"))
                .andExpect(jsonPath("$.categoryName").value("Groceries"))
                .andExpect(jsonPath("$.userName").value("John Doe"));

        verify(expenseService, times(1)).addExpense(any(ExpenseRequest.class));
    }

    @Test
    void testGetAllExpenses() throws Exception {
        when(expenseService.getAllExpenses()).thenReturn(List.of(expenseResponse));

        mockMvc.perform(get("/api/expenses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].amount").value(100.50))
                .andExpect(jsonPath("$[0].description").value("Monthly groceries"));

        verify(expenseService, times(1)).getAllExpenses();
    }

    @Test
    void testGetExpenseById() throws Exception {
        when(expenseService.getExpenseById(1L)).thenReturn(expenseResponse);

        mockMvc.perform(get("/api/expenses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(100.50))
                .andExpect(jsonPath("$.description").value("Monthly groceries"));

        verify(expenseService, times(1)).getExpenseById(1L);
    }

    @Test
    void testGetExpensesByUser() throws Exception {
        when(expenseService.getExpensesByUser(1L)).thenReturn(List.of(expenseResponse));

        mockMvc.perform(get("/api/expenses/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].userId").value(1))
                .andExpect(jsonPath("$[0].userName").value("John Doe"));

        verify(expenseService, times(1)).getExpensesByUser(1L);
    }

    @Test
    void testUpdateExpense() throws Exception {
        when(expenseService.updateExpense(eq(1L), any(ExpenseRequest.class))).thenReturn(expenseResponse);

        mockMvc.perform(put("/api/expenses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expenseRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(100.50))
                .andExpect(jsonPath("$.description").value("Monthly groceries"));

        verify(expenseService, times(1)).updateExpense(eq(1L), any(ExpenseRequest.class));
    }

    @Test
    void testDeleteExpense() throws Exception {
        doNothing().when(expenseService).deleteExpense(1L);

        mockMvc.perform(delete("/api/expenses/1"))
                .andExpect(status().isNoContent());

        verify(expenseService, times(1)).deleteExpense(1L);
    }

    @Test
    void testAddExpense_WithInvalidRequest() throws Exception {
        ExpenseRequest invalidRequest = new ExpenseRequest(
                BigDecimal.valueOf(-1), // invalid negative amount
                "", // invalid empty description
                null, // invalid null categoryId
                null, // invalid null userId
                null  // null date is allowed
        );

        mockMvc.perform(post("/api/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(expenseService, never()).addExpense(any(ExpenseRequest.class));
    }

    @Test
    void testGetExpenseById_NotFound() throws Exception {
        when(expenseService.getExpenseById(999L))
                .thenThrow(new ResourceNotFoundException("Expense not found"));

        mockMvc.perform(get("/api/expenses/999"))
                .andExpect(status().isNotFound());

        verify(expenseService, times(1)).getExpenseById(999L);
    }

    @Test
    void testUpdateExpense_NotFound() throws Exception {
        when(expenseService.updateExpense(eq(999L), any(ExpenseRequest.class)))
                .thenThrow(new ResourceNotFoundException("Expense not found"));

        mockMvc.perform(put("/api/expenses/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expenseRequest)))
                .andExpect(status().isNotFound());

        verify(expenseService, times(1)).updateExpense(eq(999L), any(ExpenseRequest.class));
    }

    @Test
    void testDeleteExpense_NotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Expense not found"))
                .when(expenseService).deleteExpense(999L);

        mockMvc.perform(delete("/api/expenses/999"))
                .andExpect(status().isNotFound());

        verify(expenseService, times(1)).deleteExpense(999L);
    }
}
