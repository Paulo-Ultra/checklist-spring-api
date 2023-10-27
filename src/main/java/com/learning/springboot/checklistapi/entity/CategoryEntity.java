package com.learning.springboot.checklistapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryEntity extends BaseEntity{

    private Long id;
    private String name;
    private List<ChecklistItemEntity> checklistItems;
}
