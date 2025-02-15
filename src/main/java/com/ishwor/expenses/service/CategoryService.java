package com.ishwor.expenses.service;

import com.ishwor.expenses.dto.response.CategoryResponse;
import com.ishwor.expenses.dto.request.CategoryRequest;

import java.util.List;


public interface CategoryService {
    CategoryResponse addCategory(CategoryRequest request);
    List<CategoryResponse> getAllCategories();
    CategoryResponse getCategoryById(Long id);
    void deleteCategory(Long id);
}