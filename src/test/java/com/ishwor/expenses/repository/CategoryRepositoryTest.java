package com.ishwor.expenses.repository;

import com.ishwor.expenses.model.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testSaveCategory() {
        Category category = new Category();
        category.setName("Groceries");
        category.setDescription("Monthly groceries shopping");

        Category savedCategory = categoryRepository.save(category);

        assertNotNull(savedCategory.getId());  
        assertEquals("Groceries", savedCategory.getName());
    }

    @Test
    void testFindById() {
        
        Category category = new Category();
        category.setName("Utilities");
        category.setDescription("Monthly bills and utilities");

        Category savedCategory = categoryRepository.save(category);

        
        Optional<Category> foundCategory = categoryRepository.findById(savedCategory.getId());

        
        assertTrue(foundCategory.isPresent());
        assertEquals("Utilities", foundCategory.get().getName());
    }

    @Test
    void testFindAll() {
        Category category1 = new Category();
        category1.setName("Transport");
        category1.setDescription("Bus, Taxi, Fuel");
        categoryRepository.save(category1);

        Category category2 = new Category();
        category2.setName("Entertainment");
        category2.setDescription("Movies, Netflix");
        categoryRepository.save(category2);

        List<Category> categories = categoryRepository.findAll();

        assertEquals(2, categories.size());
    }

    @Test
    void testUpdateCategory() {
        
        Category category = new Category();
        category.setName("Food");
        category.setDescription("Dining and restaurant expenses");
        Category savedCategory = categoryRepository.save(category);

        
        savedCategory.setName("Dining");
        savedCategory.setDescription("Updated description");
        Category updatedCategory = categoryRepository.save(savedCategory);

        
        assertEquals("Dining", updatedCategory.getName());
        assertEquals("Updated description", updatedCategory.getDescription());
    }

    @Test
    void testDeleteCategory() {
        
        Category category = new Category();
        category.setName("Shopping");
        category.setDescription("Online and offline purchases");
        Category savedCategory = categoryRepository.save(category);

        
        categoryRepository.delete(savedCategory);
        Optional<Category> deletedCategory = categoryRepository.findById(savedCategory.getId());

        
        assertFalse(deletedCategory.isPresent());
    }
}
