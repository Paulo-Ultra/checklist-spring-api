package com.learning.springboot.checklistapi.repository;

import com.learning.springboot.checklistapi.entity.ChecklistItemEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChecklistItemRepository extends PagingAndSortingRepository<ChecklistItemEntity, Long> {

    Optional<ChecklistItemEntity> findByGuid(String guid);

    List<ChecklistItemEntity> findByDescriptionAndIsCompleted(String description, Boolean isCompleted);
}
