package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryService {
    public CategoryResponseDTO getCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    public CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO deleteCategory(Long categoryId);
    CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO);
}
