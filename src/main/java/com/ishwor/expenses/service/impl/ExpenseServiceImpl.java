package com.ishwor.expenses.service.impl;

import com.ishwor.expenses.dto.request.ExpenseRequest;
import com.ishwor.expenses.dto.response.ExpenseResponse;
import com.ishwor.expenses.exception.ResourceNotFoundException;
import com.ishwor.expenses.model.Category;
import com.ishwor.expenses.model.Expense;
import com.ishwor.expenses.model.User;
import com.ishwor.expenses.repository.CategoryRepository;
import com.ishwor.expenses.repository.ExpenseRepository;
import com.ishwor.expenses.repository.UserRepository;
import com.ishwor.expenses.service.ExpenseService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository, CategoryRepository categoryRepository,UserRepository userRepository){
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ExpenseResponse addExpense(ExpenseRequest request){
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Expense expense = new Expense();
        expense.setAmount(request.amount());
        expense.setDescription(request.description());
        expense.setUser(user);
        expense.setCategory(category);
        expense.setExpenseDate(request.expenseDate());
        expense.setCreatedAt(Instant.now());

        Expense savedExpense = expenseRepository.save(expense);

        return new ExpenseResponse(
                savedExpense.getId(),
                savedExpense.getAmount(),
                savedExpense.getDescription(),
                savedExpense.getCategory().getId(),
                savedExpense.getCategory().getName(),
                savedExpense.getUser().getId(),
                savedExpense.getUser().getName(),
                savedExpense.getCreatedAt()
        );
    }

    @Override
    public List<ExpenseResponse> getAllExpenses() {
        return expenseRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private ExpenseResponse mapToResponse(Expense expense) {
        return  new ExpenseResponse(
                expense.getId(),
                expense.getAmount(),
                expense.getDescription(),
                expense.getCategory().getId(),
                expense.getCategory().getName(),
                expense.getUser().getId(),
                expense.getUser().getName(),
                expense.getCreatedAt()
        );
    }

    public List<ExpenseResponse> getExpensesByUser(Long userId){
        if(!userRepository.existsById(userId)){
            throw new ResourceNotFoundException("User not found");
        }
        List<Expense> expenses = expenseRepository.findByUserId(userId);
        return expenses.stream().map(
                expense -> new ExpenseResponse(
                        expense.getId(),
                        expense.getAmount(),
                        expense.getDescription(),
                        expense.getCategory().getId(),
                        expense.getCategory().getName(),
                        expense.getUser().getId(),
                        expense.getUser().getName(),
                        expense.getCreatedAt()
                )
        ).collect(Collectors.toList());
    }

    @Override
    public ExpenseResponse getExpenseById(Long id) {
        Expense expense = expenseRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Expense not found")
        );
        return new ExpenseResponse(
                expense.getId(),
                expense.getAmount(),
                expense.getDescription(),
                expense.getCategory().getId(),
                expense.getCategory().getName(),
                expense.getUser().getId(),
                expense.getUser().getName(),
                expense.getCreatedAt()
        );

    }

    @Override
    public void deleteExpense(Long id) {
        if(!expenseRepository.existsById(id)){
            throw new ResourceNotFoundException("Expense not found");
        }
        expenseRepository.deleteById(id);
    }

    @Override
    public ExpenseResponse updateExpense(Long id, ExpenseRequest request) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        expense.setAmount(request.amount());
        expense.setDescription(request.description());
        expense.setCategory(category);
        expense.setUpdatedAt(Instant.now());

        Expense updatedExpense = expenseRepository.save(expense);

        return new ExpenseResponse(
                updatedExpense.getId(),
                updatedExpense.getAmount(),
                updatedExpense.getDescription(),
                updatedExpense.getCategory().getId(),
                updatedExpense.getCategory().getName(),
                updatedExpense.getUser().getId(),
                updatedExpense.getUser().getName(),
                updatedExpense.getCreatedAt()
        );
    }



}
