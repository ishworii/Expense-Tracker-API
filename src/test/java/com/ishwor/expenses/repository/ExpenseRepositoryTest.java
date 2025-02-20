package com.ishwor.expenses.repository;

import com.ishwor.expenses.model.Category;
import com.ishwor.expenses.model.Expense;
import com.ishwor.expenses.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class ExpenseRepositoryTest {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private User testUser;
    private Category testCategory;

    @BeforeEach
    void setUp() {

        testUser = new User();
        testUser.setName("John Doe");
        testUser.setEmail("john.doe@example.com");
        testUser.setPassword("password");
        testUser = userRepository.save(testUser);


        testCategory = new Category();
        testCategory.setName("Groceries");
        testCategory.setDescription("Food and grocery shopping");
        testCategory = categoryRepository.save(testCategory);
    }

    @Test
    void testSaveExpense() {
        
        Expense expense = new Expense();
        expense.setAmount(BigDecimal.valueOf(50.75));
        expense.setDescription("Weekly grocery shopping");
        expense.setUser(testUser);
        expense.setCategory(testCategory);

        
        Expense savedExpense = expenseRepository.save(expense);

        
        assertNotNull(savedExpense.getId());
        assertEquals("Weekly grocery shopping", savedExpense.getDescription());
        assertEquals(testUser.getId(), savedExpense.getUser().getId());
        assertEquals(testCategory.getId(), savedExpense.getCategory().getId());
    }

    @Test
    void testFindByUserId() {
        
        Expense expense = new Expense();
        expense.setAmount(BigDecimal.valueOf(30.00));
        expense.setDescription("Dinner expenses");
        expense.setUser(testUser);
        expense.setCategory(testCategory);
        expenseRepository.save(expense);

        
        List<Expense> expenses = expenseRepository.findByUserId(testUser.getId());

        
        assertFalse(expenses.isEmpty());
        assertEquals(1, expenses.size());
        assertEquals("Dinner expenses", expenses.get(0).getDescription());
    }

    @Test
    void testFindByUser() {
        
        Expense expense1 = new Expense();
        expense1.setAmount(BigDecimal.valueOf(15.50));
        expense1.setDescription("Coffee and snacks");
        expense1.setUser(testUser);
        expense1.setCategory(testCategory);
        expenseRepository.save(expense1);

        Expense expense2 = new Expense();
        expense2.setAmount(BigDecimal.valueOf(100.00));
        expense2.setDescription("Electronics purchase");
        expense2.setUser(testUser);
        expense2.setCategory(testCategory);
        expenseRepository.save(expense2);

        
        List<Expense> expenses = expenseRepository.findByUser(testUser);

        
        assertEquals(2, expenses.size());
    }

    @Test
    void testDeleteExpense() {
        
        Expense expense = new Expense();
        expense.setAmount(BigDecimal.valueOf(45.99));
        expense.setDescription("Lunch with friends");
        expense.setUser(testUser);
        expense.setCategory(testCategory);
        Expense savedExpense = expenseRepository.save(expense);

        
        expenseRepository.delete(savedExpense);
        Optional<Expense> deletedExpense = expenseRepository.findById(savedExpense.getId());

        
        assertFalse(deletedExpense.isPresent());
    }
}
