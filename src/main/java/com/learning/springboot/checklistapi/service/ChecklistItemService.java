package com.learning.springboot.checklistapi.service;

import com.learning.springboot.checklistapi.entity.CategoryEntity;
import com.learning.springboot.checklistapi.entity.ChecklistItemEntity;
import com.learning.springboot.checklistapi.exception.ResourceNotFoundException;
import com.learning.springboot.checklistapi.repository.CategoryRepository;
import com.learning.springboot.checklistapi.repository.ChecklistItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ChecklistItemService {

    private final ChecklistItemRepository checklistItemRepository;
    private final CategoryRepository categoryRepository;
    private static final String CHECKLIST_ITEM_NOT_FOUND = "ChecklistItem not found.";

    public ChecklistItemService(ChecklistItemRepository checklistItemRepository, CategoryRepository categoryRepository){
        this.checklistItemRepository = checklistItemRepository;
        this.categoryRepository = categoryRepository;
    }

    public ChecklistItemEntity addNewChecklistItem(String description, Boolean isCompleted, LocalDate deadline, String categoryGuid){

        this.validateChecklistItemData(description, isCompleted, deadline,categoryGuid);

        CategoryEntity retrievedCategory = this.categoryRepository.findByGuid(categoryGuid)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        ChecklistItemEntity checklistItemEntity = new ChecklistItemEntity();
        checklistItemEntity.setGuid(UUID.randomUUID().toString());
        checklistItemEntity.setDescription(description);
        checklistItemEntity.setDeadline(deadline);
        checklistItemEntity.setPostedDate(LocalDate.now());
        checklistItemEntity.setCategory(retrievedCategory);
        checklistItemEntity.setIsCompleted(isCompleted);

        log.debug("Adding new checklist item [ checklistItem = {} ]", checklistItemEntity);

        return checklistItemRepository.save(checklistItemEntity);

    }

    public List<ChecklistItemEntity> findAllChecklistItems(){
        return this.checklistItemRepository.findAll();
    }

    public void deleteChecklistItem(String guid){
        validatingGuid(guid);
        ChecklistItemEntity retrivedItem = this.checklistItemRepository.findByGuid(guid)
                .orElseThrow(() -> new ResourceNotFoundException(CHECKLIST_ITEM_NOT_FOUND));

        log.debug("Deleting checklist item [guid = {} ]", guid);
        this.checklistItemRepository.delete(retrivedItem);
    }

    public ChecklistItemEntity updateChecklistItem(String guid, String description, Boolean isCompleted,
                                                   LocalDate deadline, String categoryGuid){

        this.validatingGuid(guid);
        ChecklistItemEntity retrivedItem = this.checklistItemRepository.findByGuid(guid)
                .orElseThrow(() -> new ResourceNotFoundException(CHECKLIST_ITEM_NOT_FOUND));

        if(StringUtils.hasText(description)){
            retrivedItem.setDescription(description);
        }
        if(isCompleted != null){
            retrivedItem.setIsCompleted(isCompleted);
        }
        if(deadline != null){
            retrivedItem.setDeadline(deadline);
        }

        CategoryEntity retrivedCategory = this.categoryRepository.findByGuid(categoryGuid)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found."));
            retrivedItem.setCategory(retrivedCategory);

        log.debug("Updating checklist item [ checklistItem = {} ]", retrivedItem.toString());

        return this.checklistItemRepository.save(retrivedItem);
    }

    public ChecklistItemEntity findChecklistItemByGuid(String guid){
        this.validatingGuid(guid);
        return this.checklistItemRepository.findByGuid(guid).orElseThrow(
                () -> new ResourceNotFoundException(CHECKLIST_ITEM_NOT_FOUND));
    }

    public void updateIsCompletedStatus(String guid, boolean isCompleted) {
        validatingGuid(guid);
        ChecklistItemEntity retrivedItem = this.checklistItemRepository.findByGuid(guid)
                .orElseThrow(() -> new ResourceNotFoundException(CHECKLIST_ITEM_NOT_FOUND));

        log.debug("Updating checklist item completed status [ guid = {}, isCompleted={} ]", guid, isCompleted);

        retrivedItem.setIsCompleted(isCompleted);

        this.checklistItemRepository.save(retrivedItem);
    }

    private void validateChecklistItemData(String description, Boolean isCompleted,
                                      LocalDate deadline, String guid){
        if(!StringUtils.hasText(description)){
            throw new IllegalArgumentException("Checklist item must have a description");
        }
        if(isCompleted == null){
            throw new IllegalArgumentException("Checklist item must have a flag indicating if it is completed or not");
        }
        if(deadline == null){
            throw new IllegalArgumentException("Checklist item must have a deadline");
        }
        if(!StringUtils.hasText(guid)){
            throw new IllegalArgumentException("Checklist item category guid must be provided");
        }
    }

    private void validatingGuid(String guid){
        if(!StringUtils.hasText(guid)){
            throw new IllegalArgumentException("ChecklistItem guid cannot be empty or null");
        }
    }
}