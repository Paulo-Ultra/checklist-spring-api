package com.learning.springboot.checklistapi.service;

import com.learning.springboot.checklistapi.entity.CategoryEntity;
import com.learning.springboot.checklistapi.entity.ChecklistItemEntity;
import com.learning.springboot.checklistapi.exception.ResourceNotFoundException;
import com.learning.springboot.checklistapi.repository.CategoryRepository;
import com.learning.springboot.checklistapi.repository.ChecklistItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChecklistItemServiceTest {

    private ChecklistItemService checklistItemService;
    @Mock
    private ChecklistItemRepository checklistItemRepository;
    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void initTest(){
        this.checklistItemService = new ChecklistItemService(checklistItemRepository, categoryRepository);
    }

    @Test
    void shouldCreateAChecklistSuccessfully(){

        //having
        CategoryEntity savedCategory = new CategoryEntity();
        String guid = UUID.randomUUID().toString();
        savedCategory.setGuid(guid);
        String description = "teste";
        boolean isCompleted = true;
        LocalDate deadline = LocalDate.now();

        //when
        when(categoryRepository.findByGuid(anyString())).thenReturn(Optional.of(savedCategory));
        when(checklistItemRepository.save(any(ChecklistItemEntity.class))).thenReturn(new ChecklistItemEntity());

        //then
        this.checklistItemService.addNewChecklistItem(description, isCompleted, deadline, guid);
        verify(checklistItemRepository, times(1)).save(
                argThat(checklistItemArg -> checklistItemArg.getDescription().equals(description)
                && checklistItemArg.getIsCompleted().equals(true) && checklistItemArg.getDeadline().equals(LocalDate.now())
                && checklistItemArg.getCategory().getGuid().equals(guid)));
    }

    @Test
    void shouldThrowAnExceptionWhenTryValidatingDescription(){

        CategoryEntity savedCategory = new CategoryEntity();
        String guid = UUID.randomUUID().toString();
        savedCategory.setGuid(guid);
        boolean isCompleted = true;
        LocalDate deadline = LocalDate.now();

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                this.checklistItemService.addNewChecklistItem(null, isCompleted, deadline, guid));

        assertThat(exception.getMessage(), is("Checklist item must have a description"));
    }

    @Test
    void shouldThrowAnExceptionWhenTryValidatingIsCompleted(){

        CategoryEntity savedCategory = new CategoryEntity();
        String guid = UUID.randomUUID().toString();
        savedCategory.setGuid(guid);
        String description = "Teste";
        LocalDate deadline = LocalDate.now();

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                this.checklistItemService.addNewChecklistItem(description, null, deadline, guid));

        assertThat(exception.getMessage(), is("Checklist item must have a flag indicating if it is completed or not"));
    }

    @Test
    void shouldThrowAnExceptionWhenTryValidatingDeadline(){

        CategoryEntity savedCategory = new CategoryEntity();
        String guid = UUID.randomUUID().toString();
        savedCategory.setGuid(guid);
        String description = "Teste";
        boolean isCompleted = true;

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                this.checklistItemService.addNewChecklistItem(description, isCompleted, null, guid));

        assertThat(exception.getMessage(), is("Checklist item must have a deadline"));
    }

    @Test
    void shouldThrowAnExceptionWhenTryValidatingCategoryGuid(){

        String description = "Teste";
        boolean isCompleted = true;
        LocalDate deadline = LocalDate.now();

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                this.checklistItemService.addNewChecklistItem(description, isCompleted, deadline, null));

        assertThat(exception.getMessage(), is("Checklist item category guid must be provided"));
    }

    @Test
    void shouldThrowAnExceptionWhenCategoryIsNotFound(){
        when(categoryRepository.findByGuid(anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                this.checklistItemService.addNewChecklistItem(
                        "anyValue", false, LocalDate.now(), "AnyValue"));


        assertThat(exception.getMessage(), is("Category not found"));
    }

    @Test
    void shouldFindAllChecklistItemsSuccessfully(){

        when(checklistItemRepository.findAll()).thenReturn(List.of());
        checklistItemService.findAllChecklistItems();
    }

    @Test
    void shouldDeleteChecklistItemSuccessfully(){
        //having
        ChecklistItemEntity checklistItem = new ChecklistItemEntity();
        String guid = UUID.randomUUID().toString();
        checklistItem.setGuid(guid);

        //when
        when(checklistItemRepository.findByGuid(anyString())).thenReturn(Optional.of(checklistItem));

        //then
        checklistItemService.deleteChecklistItem(guid);
    }

    @Test
    void shouldThrowAnExceptionWhenChecklistItemIsNotFound(){
        when(checklistItemRepository.findByGuid(anyString())).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> this.checklistItemService.deleteChecklistItem("AnyValue"));

        assertThat(exception.getMessage(), is("ChecklistItem not found."));
    }

    @Test
    void shouldThrowAnExceptionWhenValidatingGuidIsNull(){
        Exception exception = assertThrows(IllegalArgumentException.class, () -> this.checklistItemService.deleteChecklistItem(null));

        assertThat(exception.getMessage(), is("ChecklistItem guid cannot be empty or null"));
    }
    @Test
    void shouldUpdateAChecklistSuccessfully(){

        //having
        ChecklistItemEntity checklistItem = new ChecklistItemEntity();
        String guid = UUID.randomUUID().toString();
        checklistItem.setGuid(guid);
        CategoryEntity savedCategory = new CategoryEntity();
        String categoryGuid = UUID.randomUUID().toString();
        savedCategory.setGuid(categoryGuid);
        String description = "teste";
        boolean isCompleted = true;
        LocalDate deadline = LocalDate.now();

        //when
        when(checklistItemRepository.findByGuid(anyString())).thenReturn(Optional.of(checklistItem));
        when(categoryRepository.findByGuid(anyString())).thenReturn(Optional.of(savedCategory));
        when(checklistItemRepository.save(any(ChecklistItemEntity.class))).thenReturn(new ChecklistItemEntity());

        //then
        this.checklistItemService.updateChecklistItem(guid, description, isCompleted, deadline, categoryGuid);
        verify(checklistItemRepository, times(1)).save(
                argThat(checklistItemArg ->  checklistItemArg.getGuid().equals(guid) && checklistItemArg.getDescription().equals(description)
                        && checklistItemArg.getIsCompleted().equals(true) && checklistItemArg.getDeadline().equals(LocalDate.now())
                        && checklistItemArg.getCategory().getGuid().equals(categoryGuid)));
    }

    //Outra forma de pegar os parâmetros usando o verify com ArgumentCaptor
    @Test
    void shouldUpdateAChecklistSuccessfully2(){

        //having
        ChecklistItemEntity checklistItem = new ChecklistItemEntity();
        String guid = UUID.randomUUID().toString();
        checklistItem.setGuid(guid);
        CategoryEntity savedCategory = new CategoryEntity();
        String categoryGuid = UUID.randomUUID().toString();
        savedCategory.setGuid(categoryGuid);
        String description = "teste";
        boolean isCompleted = true;
        LocalDate deadline = LocalDate.now();

        //when
        when(checklistItemRepository.findByGuid(anyString())).thenReturn(Optional.of(checklistItem));
        when(categoryRepository.findByGuid(anyString())).thenReturn(Optional.of(savedCategory));
//        when(checklistItemRepository.save(any(ChecklistItemEntity.class))).thenReturn(new ChecklistItemEntity());

        ArgumentCaptor<ChecklistItemEntity> argumentCaptor = ArgumentCaptor.forClass(ChecklistItemEntity.class);

        //then
        this.checklistItemService.updateChecklistItem(guid, description, isCompleted, deadline, categoryGuid);
        verify(checklistItemRepository, times(1)).save(argumentCaptor.capture());
        ChecklistItemEntity savedChecklistItem = argumentCaptor.getValue();
        assertThat(savedChecklistItem.getGuid(), is(guid));
        assertThat(savedChecklistItem.getDescription(), is(description));
        assertThat(savedChecklistItem.getIsCompleted(), is(true));
        assertThat(savedChecklistItem.getDeadline(), is(LocalDate.now()));
        assertThat(savedChecklistItem.getCategory(), is(notNullValue()));
        assertThat(savedChecklistItem.getCategory().getGuid(), is(categoryGuid));
    }

    @Test
    void shouldThrowAnExceptionWhenUpdatingChecklistItemIsNotFound(){
        when(checklistItemRepository.findByGuid(anyString())).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> this.checklistItemService.updateChecklistItem(
                "AnyValue", "anyValue", true, LocalDate.now(), "Anyvalue"));

        assertThat(exception.getMessage(), is("ChecklistItem not found."));
    }

    @Test
    void shouldThrowAnExceptionWhenUpdatingChecklistItemAndCategoryIsNotFound(){
        //having
        ChecklistItemEntity checklistItem = new ChecklistItemEntity();
        String guid = UUID.randomUUID().toString();
        checklistItem.setGuid(guid);
        CategoryEntity savedCategory = new CategoryEntity();
        String categoryGuid = "";
        savedCategory.setGuid(categoryGuid);

        //when
        when(checklistItemRepository.findByGuid(anyString())).thenReturn(Optional.of(checklistItem));
        when(categoryRepository.findByGuid(anyString())).thenReturn(Optional.empty());
        //when(checklistItemRepository.save(any(ChecklistItemEntity.class))).thenReturn(new ChecklistItemEntity());

        //then
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> this.checklistItemService.updateChecklistItem(
                "AnyValue", "AnyValue", true, LocalDate.now(), categoryGuid));
        assertThat(exception.getMessage(), is("Category not found."));
    }

    @Test
    void shouldFindChecklistItemByGuidSuccessfully(){
        //having
        ChecklistItemEntity checklistItem = new ChecklistItemEntity();
        String guid = UUID.randomUUID().toString();
        checklistItem.setGuid(guid);

        //when
        when(checklistItemRepository.findByGuid(anyString())).thenReturn(Optional.of(checklistItem));

        //then
        checklistItemService.findChecklistItemByGuid(guid);
    }

    @Test
    void shouldThrowAnExceptionWhenFindChecklistItemByGuidIsNotFound(){
        when(checklistItemRepository.findByGuid(anyString())).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> this.checklistItemService.findChecklistItemByGuid("AnyValue"));

        assertThat(exception.getMessage(), is("ChecklistItem not found."));
    }

    @Test
    void shouldUpdateCompletedStatusSuccessfully(){
        //having
        ChecklistItemEntity checklistItem = new ChecklistItemEntity();
        String guid = UUID.randomUUID().toString();
        checklistItem.setGuid(guid);
        Boolean isCompleted = true;

        //when
        when(checklistItemRepository.findByGuid(anyString())).thenReturn(Optional.of(checklistItem));

        //then
        checklistItemService.updateIsCompletedStatus(guid, isCompleted);
    }

    @Test
    void shouldThrowAnExceptionWhenUpdateCompletedStatusIsNotFound(){
        when(checklistItemRepository.findByGuid(anyString())).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> this.checklistItemService.updateIsCompletedStatus("AnyValue", true));

        assertThat(exception.getMessage(), is("ChecklistItem not found."));
    }
}