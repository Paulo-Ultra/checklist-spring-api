package com.learning.springboot.checklistapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChecklistItemEntity extends BaseEntity{

    private Long id;
    private Boolean isCompleted;
    private String description;
    private LocalTime deadline;
    private LocalTime postedDate;
    private CategoryEntity category;
}
