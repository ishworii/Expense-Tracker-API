package com.ishwor.expenses.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ishwor.expenses.dto.request.CategoryRequest;
import com.ishwor.expenses.dto.response.CategoryResponse;
import com.ishwor.expenses.exception.ResourceNotFoundException;
import com.ishwor.expenses.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private CategoryResponse categoryResponse;
    private CategoryRequest categoryRequest;

    @BeforeEach
    void setUp() {
        categoryRequest = new CategoryRequest("Groceries", "Food and grocery shopping");
        categoryResponse = new CategoryResponse(1L, "Groceries", "Food and grocery shopping", Instant.now());
    }

    @Test
    void testAddCategory() throws Exception {
        when(categoryService.addCategory(any(CategoryRequest.class))).thenReturn(categoryResponse);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Groceries"))
                .andExpect(jsonPath("$.description").value("Food and grocery shopping"));

        verify(categoryService, times(1)).addCategory(any(CategoryRequest.class));
    }

    @Test
    void testGetAllCategories() throws Exception {
        when(categoryService.getAllCategories()).thenReturn(List.of(categoryResponse));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Groceries"))
                .andExpect(jsonPath("$[0].description").value("Food and grocery shopping"));

        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    void testGetCategoryById() throws Exception {
        when(categoryService.getCategoryById(1L)).thenReturn(categoryResponse);

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Groceries"))
                .andExpect(jsonPath("$.description").value("Food and grocery shopping"));

        verify(categoryService, times(1)).getCategoryById(1L);
    }

    @Test
    void testDeleteCategory() throws Exception {
        doNothing().when(categoryService).deleteCategory(1L);

        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isNoContent());

        verify(categoryService, times(1)).deleteCategory(1L);
    }

    @Test
    void testAddCategory_WithInvalidRequest() throws Exception {
        CategoryRequest invalidRequest = new CategoryRequest("", "");

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(categoryService, never()).addCategory(any(CategoryRequest.class));
    }

    @Test
    void testGetCategoryById_NotFound() throws Exception {
        when(categoryService.getCategoryById(999L))
                .thenThrow(new ResourceNotFoundException("Category not found"));

        mockMvc.perform(get("/api/categories/999"))
                .andExpect(status().isNotFound());

        verify(categoryService, times(1)).getCategoryById(999L);
    }

    @Test
    void testDeleteCategory_NotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Category not found"))
                .when(categoryService).deleteCategory(999L);

        mockMvc.perform(delete("/api/categories/999"))
                .andExpect(status().isNotFound());

        verify(categoryService, times(1)).deleteCategory(999L);
    }

    @Test
    void testGetAllCategories_EmptyList() throws Exception {
        when(categoryService.getAllCategories()).thenReturn(List.of());

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));

        verify(categoryService, times(1)).getAllCategories();
    }
}
