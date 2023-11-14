package com.learning.springboot.checklistapi.service;

import com.learning.springboot.checklistapi.entity.CategoryEntity;
import com.learning.springboot.checklistapi.repository.CategoryRepository;
import com.learning.springboot.checklistapi.repository.ChecklistItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    void shouldCreateACategorySucessfully(){

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
    void shouldThrowAnExceptionWhenCategoryNameIdNullorEmpty(){
        Exception exception = assertThrows(IllegalArgumentException.class, () -> this.categoryService.addNewCategory(null));

        assertThat(exception.getMessage(), is("Category name cannot be empty or null"));
    }
}
