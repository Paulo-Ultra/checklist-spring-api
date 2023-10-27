package com.learning.springboot.checklistapi.service;

import com.learning.springboot.checklistapi.entity.CategoryEntity;
import com.learning.springboot.checklistapi.entity.ChecklistItemEntity;
import com.learning.springboot.checklistapi.exception.CategoryServiceException;
import com.learning.springboot.checklistapi.exception.ResourceNotFoundException;
import com.learning.springboot.checklistapi.repository.CategoryRepository;
import com.learning.springboot.checklistapi.repository.ChecklistItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class CategoryService {

    private final ChecklistItemRepository checklistItemRepository;
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository, ChecklistItemRepository checklistItemRepository) {
        this.categoryRepository = categoryRepository;
        this.checklistItemRepository = checklistItemRepository;
    }

    public CategoryEntity addNewCategory(String name){
        if(!StringUtils.hasText(name)){
            throw new IllegalArgumentException("Category name cannot be empty or null");
        }
        CategoryEntity newCategory = new CategoryEntity();
        newCategory.setGuid(UUID.randomUUID().toString());
        newCategory.setName(name);

        log.debug("Adding new Category with name [name = {}]", name);
        return this.categoryRepository.save(newCategory);
    }

    public CategoryEntity updateCategory(String guid, String name){
        if(guid == null || StringUtils.hasText(name)){
            throw new IllegalArgumentException("Invalid parameters provided to update a category");
        }
        CategoryEntity retrivedCategory = this.categoryRepository.findByGuid(guid).orElseThrow(
                () -> new ResourceNotFoundException("Category not found."));
        retrivedCategory.setName(name);
        log.debug("Updating category [ guid  {}, newName = {}", guid, name);

        return this.categoryRepository.save(retrivedCategory);
    }

    public void deleteCategory(String guid){
        if(StringUtils.hasText(guid)){
            throw new IllegalArgumentException("Category guid cannot be empty or null");
        }
        CategoryEntity retrivedCategory = this.categoryRepository.findByGuid(guid).orElseThrow(
                () -> new ResourceNotFoundException("Category not found."));

        List<ChecklistItemEntity> checklistItems = this.checklistItemRepository.findByCategoryGuid(guid);
        if(!CollectionUtils.isEmpty(checklistItems)){
            throw new CategoryServiceException("It is not possible to delete given category as it has been used by checklist items");
        }
        this.categoryRepository.delete(retrivedCategory);
    }

    public List<CategoryEntity> findAllCategories(){
        return this.categoryRepository.findAll();
    }

    public CategoryEntity findCategoryByGuid(String guid){
        if(StringUtils.hasText(guid)){
            throw new IllegalArgumentException("Category guid cannot be empty or null");
        }
        log.debug("Deleting category [guid {} ]", guid);
        return this.categoryRepository.findByGuid(guid).orElseThrow(
                () -> new ResourceNotFoundException("Category not found."));
    }
}
