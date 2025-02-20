package com.ishwor.expenses.service;

import com.ishwor.expenses.dto.request.ExpenseRequest;
import com.ishwor.expenses.dto.response.ExpenseResponse;
import com.ishwor.expenses.exception.ResourceNotFoundException;
import com.ishwor.expenses.model.Category;
import com.ishwor.expenses.model.Expense;
import com.ishwor.expenses.model.User;
import com.ishwor.expenses.repository.CategoryRepository;
import com.ishwor.expenses.repository.ExpenseRepository;
import com.ishwor.expenses.repository.UserRepository;
import com.ishwor.expenses.service.impl.ExpenseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ExpenseServiceImpl expenseService;

    private Expense testExpense;
    private ExpenseRequest expenseRequest;
    private User testUser;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("John Doe");
        testUser.setEmail("john.doe@example.com");
        testUser.setPassword("securepassword123");

        
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Groceries");
        testCategory.setDescription("Food and grocery shopping");

        
        testExpense = new Expense();
        testExpense.setId(1L);
        testExpense.setAmount(BigDecimal.valueOf(50.00));
        testExpense.setDescription("Weekly groceries");
        testExpense.setUser(testUser);
        testExpense.setCategory(testCategory);
        testExpense.setExpenseDate(LocalDate.now());
        testExpense.setCreatedAt(Instant.now());

        
        expenseRequest = new ExpenseRequest(
                BigDecimal.valueOf(50.00),
                "Weekly groceries",
                1L, 
                1L, 
                LocalDate.now()
        );
    }

    @Test
    void testAddExpense() {
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(expenseRepository.save(any(Expense.class))).thenReturn(testExpense);

        
        ExpenseResponse response = expenseService.addExpense(expenseRequest);

        
        assertNotNull(response);
        assertEquals(BigDecimal.valueOf(50.00), response.amount());
        assertEquals("Weekly groceries", response.description());
        verify(expenseRepository, times(1)).save(any(Expense.class));
    }

    @Test
    void testAddExpense_UserNotFound() {
        
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        
        assertThrows(ResourceNotFoundException.class, () -> expenseService.addExpense(expenseRequest));
    }

    @Test
    void testAddExpense_CategoryNotFound() {
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        
        assertThrows(ResourceNotFoundException.class, () -> expenseService.addExpense(expenseRequest));
    }

    @Test
    void testGetAllExpenses() {
        
        when(expenseRepository.findAll()).thenReturn(List.of(testExpense));

        
        List<ExpenseResponse> expenses = expenseService.getAllExpenses();

        
        assertFalse(expenses.isEmpty());
        assertEquals(1, expenses.size());
        assertEquals("Weekly groceries", expenses.get(0).description());
        verify(expenseRepository, times(1)).findAll();
    }

    @Test
    void testGetExpensesByUser_Success() {
        
        when(userRepository.existsById(1L)).thenReturn(true);
        when(expenseRepository.findByUserId(1L)).thenReturn(List.of(testExpense));

        
        List<ExpenseResponse> expenses = expenseService.getExpensesByUser(1L);

        
        assertFalse(expenses.isEmpty());
        assertEquals(1, expenses.size());
        assertEquals("Weekly groceries", expenses.get(0).description());
        verify(expenseRepository, times(1)).findByUserId(1L);
    }

    @Test
    void testGetExpensesByUser_UserNotFound() {
        
        when(userRepository.existsById(99L)).thenReturn(false);

        
        assertThrows(ResourceNotFoundException.class, () -> expenseService.getExpensesByUser(99L));
    }

    @Test
    void testGetExpenseById_Success() {
        
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(testExpense));

        
        ExpenseResponse response = expenseService.getExpenseById(1L);

        
        assertNotNull(response);
        assertEquals("Weekly groceries", response.description());
        verify(expenseRepository, times(1)).findById(1L);
    }

    @Test
    void testGetExpenseById_NotFound() {
        
        when(expenseRepository.findById(99L)).thenReturn(Optional.empty());

        
        assertThrows(ResourceNotFoundException.class, () -> expenseService.getExpenseById(99L));
    }

    @Test
    void testDeleteExpense_Success() {
        
        when(expenseRepository.existsById(1L)).thenReturn(true);

        
        expenseService.deleteExpense(1L);

        
        verify(expenseRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteExpense_NotFound() {
        
        when(expenseRepository.existsById(99L)).thenReturn(false);

        
        assertThrows(ResourceNotFoundException.class, () -> expenseService.deleteExpense(99L));
        verify(expenseRepository, never()).deleteById(99L);
    }

    @Test
    void testUpdateExpense_Success() {
        
        ExpenseRequest updatedRequest = new ExpenseRequest(
                BigDecimal.valueOf(75.00),
                "Updated groceries",
                1L, 1L, LocalDate.now()
        );

        
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(testExpense));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(expenseRepository.save(any(Expense.class))).thenReturn(testExpense);

        
        ExpenseResponse response = expenseService.updateExpense(1L, updatedRequest);

        
        assertNotNull(response);
        assertEquals("Updated groceries", response.description());
        verify(expenseRepository, times(1)).save(any(Expense.class));
    }

    @Test
    void testUpdateExpense_NotFound() {
        
        when(expenseRepository.findById(99L)).thenReturn(Optional.empty());

        
        assertThrows(ResourceNotFoundException.class, () -> expenseService.updateExpense(99L, expenseRequest));
    }
}
