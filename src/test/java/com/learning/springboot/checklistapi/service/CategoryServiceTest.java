package com.learning.springboot.checklistapi.service;

import com.learning.springboot.checklistapi.entity.CategoryEntity;
import com.learning.springboot.checklistapi.entity.ChecklistItemEntity;
import com.learning.springboot.checklistapi.exception.ResourceNotFoundException;
import com.learning.springboot.checklistapi.exception.ValidationException;
import com.learning.springboot.checklistapi.repository.CategoryRepository;
import com.learning.springboot.checklistapi.repository.ChecklistItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    private CategoryService categoryService;
    @Mock
    private ChecklistItemRepository checklistItemRepository;
    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void initTest(){
        this.categoryService = new CategoryService(categoryRepository, checklistItemRepository);
    }

    @Test
    void shouldCreateACategorySuccessfully(){

        //having
        String categoryName = "Teste";
        when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(new CategoryEntity());

        //when
        CategoryEntity category = this.categoryService.addNewCategory(categoryName);

        //then
        assertNotNull(category);
        verify(categoryRepository, times(1)).save(
                argThat(categoryEntityArg -> categoryEntityArg.getName().equals("Teste")
                        && categoryEntityArg.getGuid() != null)
        );
    }

    @Test
    void shouldThrowAnExceptionWhenCategoryNameIsNullOrEmpty(){
        Exception exception = assertThrows(IllegalArgumentException.class, () -> this.categoryService.addNewCategory(null));

        assertThat(exception.getMessage(), is("Category name cannot be empty or null"));
    }

    @Test
    void shouldUpdateCategorySuccessfully(){
        //having
        String guid = UUID.randomUUID().toString();
        String name ="other";
        CategoryEntity savedCategory = new CategoryEntity();
        savedCategory.setGuid(guid);
        savedCategory.setName(name);

        //when
        when(categoryRepository.findByGuid(guid)).thenReturn(Optional.of(savedCategory));
        when(categoryRepository.save((any(CategoryEntity.class)))).thenReturn(new CategoryEntity());

        //then
        CategoryEntity category = this.categoryService.updateCategory(guid, name);
        assertNotNull(category);
        verify(categoryRepository, times(1)).save(
                argThat(categoryArg -> categoryArg.getName().equals(name)
                && categoryArg.getGuid().equals(guid))
        );
    }

    @Test
    void shouldThrowAnExceptionWhenTryToUpdateAndCategoryGuidIsNullOrEmpty(){
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                this.categoryService.updateCategory("", "Sample Name"));

        assertThat(exception.getMessage(), is("Invalid parameters provided to update a category"));
    }

    @Test
    void shouldThrowAnExceptionWhenTryToUpdateAndCategoryNameIsNullOrEmpty(){
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                this.categoryService.updateCategory("anyValue", ""));

        assertThat(exception.getMessage(), is("Invalid parameters provided to update a category"));
    }

    @Test
    void shouldThrowAnExceptionWhenTryToUpdateAndCategoryAndItDoesNotExist(){
        when(categoryRepository.findByGuid(anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                this.categoryService.updateCategory("anyValue", "anyName"));

        assertThat(exception.getMessage(), is("Category not found."));
    }

    @Test
    void shouldDeleteCategorySuccessfully(){
        //having
        CategoryEntity savedCategory = new CategoryEntity();
        String guid = UUID.randomUUID().toString();
        savedCategory.setGuid(guid);

        //when
        when(categoryRepository.findByGuid(anyString())).thenReturn(Optional.of(savedCategory));

        //then
        categoryService.deleteCategory(guid);
    }
    @Test
    void shouldThrowAnExceptionWhenTryToDeleteAndCategoryAndItDoesNotExist(){
        when(categoryRepository.findByGuid(anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                this.categoryService.deleteCategory("anyValue"));

        assertThat(exception.getMessage(), is("Category not found."));
    }

    @Test
    void shouldThrowAnExceptionWhenTryToDeleteAndCategoryAndChecklistItemExist(){
        ChecklistItemEntity checklistItem = new ChecklistItemEntity();
        CategoryEntity savedCategory = new CategoryEntity();
        String guid = "anyValue";
        checklistItem.setGuid(guid);

        when(categoryRepository.findByGuid(guid)).thenReturn(Optional.of(savedCategory));
        when(checklistItemRepository.findByCategoryGuid(guid)).thenReturn(List.of(checklistItem));

        Exception exception = assertThrows(ValidationException.class, () ->
                this.categoryService.deleteCategory("anyValue"));

        assertThat(exception.getMessage(), is("It is not possible to delete given category as it has been used by checklist items"));
    }
    @Test
    void shouldThrowAnExceptionWhenTryValidatingGuid(){
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                this.categoryService.findCategoryByGuid(null));

        assertThat(exception.getMessage(), is("Category guid cannot be empty or null"));
    }
    @Test
    void shouldFindAllCategoriesSuccessfully(){

        when(categoryRepository.findAll()).thenReturn(List.of());
        categoryService.findAllCategories();
    }

    @Test
    void shouldThrowAnExceptionWhenTryToFindCategoryByGuid(){
        when(categoryRepository.findByGuid(anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                this.categoryService.findCategoryByGuid("anyValue"));

        assertThat(exception.getMessage(), is("Category not found."));
    }

    @Test
    void shouldFindCategoryByGuid(){
        //having
        String guid = UUID.randomUUID().toString();
        String name ="other";
        CategoryEntity savedCategory = new CategoryEntity();
        savedCategory.setGuid(guid);
        savedCategory.setName(name);

        //when
        when(categoryRepository.findByGuid(guid)).thenReturn(Optional.of(savedCategory));

        //then
        categoryService.findCategoryByGuid(guid);
    }
}