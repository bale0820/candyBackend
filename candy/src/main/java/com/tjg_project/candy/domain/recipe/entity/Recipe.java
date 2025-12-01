package com.tjg_project.candy.domain.recipe.entity;

import com.tjg_project.candy.domain.category.entity.CategorySub;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "recipe")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(name = "cook_time")
    private Integer cookTime;

    @Column(length = 300)
    private String summary;

    @Column(name = "rating", columnDefinition = "DOUBLE DEFAULT 0")
    private double rating;

    @Column(name = "review_count", columnDefinition = "INT DEFAULT 0")
    private int reviewCount;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "difficulty", length = 20)
    private String difficulty;  // 🔥 추가됨!

    @Lob
    private String ingredients;

    @Lob
    private String steps;

    @Column(length = 300)
    private String tips;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id", nullable = false)
    private CategorySub subCategory;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @Column(name = "youtube_url", length = 255)
    private String youtubeUrl;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


}
