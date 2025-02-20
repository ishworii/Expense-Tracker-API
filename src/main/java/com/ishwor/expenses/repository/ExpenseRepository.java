package com.ishwor.expenses.repository;

import com.ishwor.expenses.model.Expense;
import com.ishwor.expenses.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserId(Long userId);

    List<Expense> findByUser(User user);
}