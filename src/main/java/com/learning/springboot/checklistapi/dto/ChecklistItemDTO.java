package com.learning.springboot.checklistapi.dto;

import com.learning.springboot.checklistapi.entity.ChecklistItemEntity;
import lombok.Builder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Builder
public record ChecklistItemDTO(
        String guid,
        @NotBlank(message = "Checklist item description cannot be either null or empty")
        String description,
        @NotNull(message = "Is completed is mandatory")
        Boolean isCompleted,
        @NotNull(message = "Deadline is mandatory")
        LocalDate deadline,
        LocalDate postDate,
        CategoryDTO categoryDTO) {

    public static ChecklistItemDTO toDTO(ChecklistItemEntity checklistItemEntity) {
        return ChecklistItemDTO.builder()
                .guid(checklistItemEntity.getGuid())
                .description(checklistItemEntity.getDescription())
                .isCompleted(checklistItemEntity.getIsCompleted())
                .deadline(checklistItemEntity.getDeadline())
                .postDate(checklistItemEntity.getPostedDate())
                .categoryDTO(checklistItemEntity.getCategory() != null ?
                        CategoryDTO.builder()
                                .guid(checklistItemEntity.getCategory().getGuid())
                                .guid(checklistItemEntity.getCategory().getName())
                                .build() : null)
                .build();
    }
}
