package com.learning.springboot.checklistapi.dto;

import com.learning.springboot.checklistapi.entity.CategoryEntity;
import lombok.Builder;

import javax.validation.constraints.NotBlank;

@Builder
public record CategoryDTO(
        String guid,
        @NotBlank(message = "Category name cannot be either null or empty")
        String name) {

    public static CategoryDTO toDTO(CategoryEntity categoryEntity){
        return CategoryDTO.builder()
                .guid(categoryEntity.getGuid())
                .name(categoryEntity.getName())
                .build();
    }
}
