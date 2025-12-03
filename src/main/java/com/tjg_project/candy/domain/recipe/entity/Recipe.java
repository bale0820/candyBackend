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

    // 제목 (짧은 문자열 → VARCHAR)
    @Column(nullable = false, length = 150)
    private String title;

    // 이미지 URL (Cloudinary/Supabase는 300~500자 넘음)
    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "cook_time")
    private Integer cookTime;

    // 요약문 (300 → 500으로 확장)
    @Column(length = 500)
    private String summary;

    // 기본값은 엔티티 안에서 직접 처리하는 게 안정적 (JPA 권장)
    @Builder.Default
    @Column(name = "rating")
    private double rating = 0;

    @Builder.Default
    @Column(name = "review_count")
    private int reviewCount = 0;

    // 본문 — 긴 텍스트는 TEXT로 직접 지정
    @Column(columnDefinition = "TEXT")
    private String content;

    // 난이도 (길이 20 → 50으로 증가)
    @Column(name = "difficulty", length = 50)
    private String difficulty;

    // 재료 목록 — 긴 텍스트
    @Column(columnDefinition = "TEXT")
    private String ingredients;

    // 요리 단계 — 긴 텍스트
    @Column(columnDefinition = "TEXT")
    private String steps;

    // 요리 꿀팁 — 300 → 500 확장
    @Column(length = 500)
    private String tips;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id", nullable = false)
    private CategorySub subCategory;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 유튜브 URL 길이 제한 확장
    @Column(name = "youtube_url", length = 500)
    private String youtubeUrl;

    // 생성 시 createdAt + updatedAt 둘 다 설정
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // 수정 시 updatedAt 갱신
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
