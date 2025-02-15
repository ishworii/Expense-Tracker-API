package com.ishwor.expenses.service;


import com.ishwor.expenses.dto.request.ExpenseRequest;
import com.ishwor.expenses.dto.response.ExpenseResponse;

import java.util.List;

public interface ExpenseService {
    ExpenseResponse addExpense(ExpenseRequest request);
    List<ExpenseResponse> getAllExpenses();
    List<ExpenseResponse> getExpensesByUser(Long userId);
    ExpenseResponse getExpenseById(Long id);
    void deleteExpense(Long id);
    ExpenseResponse updateExpense(Long id, ExpenseRequest request);
}