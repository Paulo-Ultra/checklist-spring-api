package com.learning.springboot.checklistapi.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Entity(name = "Checklist Item")
@Table(indexes = {@Index(name = "IDX_GUID_CK_IT", columnList = "guid")})
public class ChecklistItemEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean isCompleted;

    private String description;

    private LocalDate deadline;

    private LocalDate postedDate;

    @ManyToOne
    private CategoryEntity category;
}
