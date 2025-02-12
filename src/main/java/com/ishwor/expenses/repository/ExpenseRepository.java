package com.ishwor.expenses.repository;

import com.ishwor.expenses.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}