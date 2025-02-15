package com.ishwor.expenses.service.impl;

import com.ishwor.expenses.dto.response.CategoryResponse;
import com.ishwor.expenses.dto.request.CategoryRequest;
import com.ishwor.expenses.model.Category;
import com.ishwor.expenses.repository.CategoryRepository;
import com.ishwor.expenses.service.CategoryService;
import com.ishwor.expenses.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryResponse addCategory(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.name());
        category.setDescription(request.description());
        category.setCreatedAt(Instant.now());

        Category savedCategory = categoryRepository.save(category);
        return new CategoryResponse(
                savedCategory.getId(),
                savedCategory.getName(),
                savedCategory.getDescription(),
                savedCategory.getCreatedAt()
        );
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream().map(
                cat -> new CategoryResponse(
                        cat.getId(),
                        cat.getName(),
                        cat.getDescription(),
                        cat.getCreatedAt()
                )
        ).collect(Collectors.toList());
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category not found")
        );
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getCreatedAt()
        );
    }

    @Override
    public void deleteCategory(Long id) {
        if(!categoryRepository.existsById(id)){
            throw new ResourceNotFoundException("Category not found");
        }
        categoryRepository.deleteById(id);
    }

}



