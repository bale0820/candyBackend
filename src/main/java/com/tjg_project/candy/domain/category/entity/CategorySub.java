package com.tjg_project.candy.domain.category.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "category_sub")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CategorySub {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(name = "display_order")
    private Integer displayOrder = 0;

    private boolean isUsed;

    // 중분류 → 대분류 N:1 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_id")
    @JsonBackReference
    private CategoryMain mainCategory;
}