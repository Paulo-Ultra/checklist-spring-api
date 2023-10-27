package com.learning.springboot.checklistapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Data
@Entity(name = "Checklist Item")
@Table(indexes = {@Index(name = "IDX_GUID_CK_IT", columnList = "guid")})
public class ChecklistItemEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean isCompleted;

    private String description;

    private LocalTime deadline;

    private LocalTime postedDate;

    @ManyToOne
    private CategoryEntity category;
}
