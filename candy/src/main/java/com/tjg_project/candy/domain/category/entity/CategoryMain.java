package com.tjg_project.candy.domain.category.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "category_main")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CategoryMain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(name = "display_order")
    private Integer displayOrder = 0;

    private boolean isUsed;

    // 대분류와 중분류 1:N 매핑
    @OneToMany(mappedBy = "mainCategory")
    @JsonManagedReference
    private List<CategorySub> subCategories;
}