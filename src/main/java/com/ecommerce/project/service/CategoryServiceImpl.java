package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourseNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponseDTO;
import com.ecommerce.project.repositories.CategoryRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public CategoryResponseDTO getCategories(Integer pageNumber, Integer pageSize,  String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);
        List<Category> categories = categoryPage.getContent();

        if (categories.isEmpty()) {
            throw new APIException("No categories found");
        }
        List<CategoryDTO> categoryDTO = categories.stream().map(c -> modelMapper.map(c, CategoryDTO.class)).toList();
        return new CategoryResponseDTO(categoryDTO, categoryPage.getNumber(), categoryPage.getSize(), categoryPage.getTotalElements(), categoryPage.getTotalPages(), categoryPage.isLast() );
    }

    @Transactional
    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {

        //here we are not checking if the categoryName already exits
        //Now we will have to map categoryDTO to category
        Category newCategory = modelMapper.map(categoryDTO, Category.class);
        if (categoryRepository.findByCategoryName(newCategory.getCategoryName()) != null) {
            throw new APIException("Category with this name " +  newCategory.getCategoryName() + " already exists");
        }
        categoryRepository.save(newCategory);
        return modelMapper.map(newCategory, CategoryDTO.class);
    }

    @Transactional
    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourseNotFoundException("Category", "CategoryId", categoryId));
        categoryRepository.delete(category);
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Transactional
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) {

        Category toUpdateCategory = modelMapper.map(categoryDTO, Category.class);
        Category savedCategory = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new ResourseNotFoundException("category", "categoryId", categoryId));

        toUpdateCategory.setCategoryId(categoryId);
        savedCategory = categoryRepository.save(toUpdateCategory);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }
}
