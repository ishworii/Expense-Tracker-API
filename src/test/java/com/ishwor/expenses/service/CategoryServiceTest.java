package com.ishwor.expenses.service;

import com.ishwor.expenses.dto.request.CategoryRequest;
import com.ishwor.expenses.dto.response.CategoryResponse;
import com.ishwor.expenses.exception.ResourceNotFoundException;
import com.ishwor.expenses.model.Category;
import com.ishwor.expenses.repository.CategoryRepository;
import com.ishwor.expenses.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category testCategory;
    private CategoryRequest categoryRequest;

    @BeforeEach
    void setUp() {
        
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Groceries");
        testCategory.setDescription("Food and grocery shopping");
        testCategory.setCreatedAt(Instant.now());

        categoryRequest = new CategoryRequest("Groceries", "Food and grocery shopping");
    }

    @Test
    void testAddCategory() {
        
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);

        
        CategoryResponse response = categoryService.addCategory(categoryRequest);

        
        assertNotNull(response);
        assertEquals("Groceries", response.name());
        assertEquals("Food and grocery shopping", response.description());
        verify(categoryRepository, times(1)).save(any(Category.class)); 
    }

    @Test
    void testGetAllCategories() {
        
        when(categoryRepository.findAll()).thenReturn(List.of(testCategory));

        
        List<CategoryResponse> categories = categoryService.getAllCategories();

        
        assertFalse(categories.isEmpty());
        assertEquals(1, categories.size());
        assertEquals("Groceries", categories.get(0).name());
        verify(categoryRepository, times(1)).findAll(); 
    }

    @Test
    void testGetCategoryById_Success() {
        
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));

        
        CategoryResponse response = categoryService.getCategoryById(1L);

        
        assertNotNull(response);
        assertEquals("Groceries", response.name());
        verify(categoryRepository, times(1)).findById(1L); 
    }

    @Test
    void testGetCategoryById_NotFound() {
        
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        
        assertThrows(ResourceNotFoundException.class, () -> categoryService.getCategoryById(99L));
        verify(categoryRepository, times(1)).findById(99L);
    }

    @Test
    void testDeleteCategory_Success() {
        
        when(categoryRepository.existsById(1L)).thenReturn(true);

        
        categoryService.deleteCategory(1L);

        
        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCategory_NotFound() {
        
        when(categoryRepository.existsById(99L)).thenReturn(false);

        
        assertThrows(ResourceNotFoundException.class, () -> categoryService.deleteCategory(99L));
        verify(categoryRepository, times(1)).existsById(99L);
        verify(categoryRepository, never()).deleteById(99L);
    }
}
