package com.tjg_project.candy.domain.recipe.dto;

import com.tjg_project.candy.domain.recipe.entity.RecipeReview;
import lombok.Data;

@Data
public class ReviewResponseDto {

    private Long id;
    private String username;
    private double rating;
    private String content;
    private String createdAt;

    public ReviewResponseDto(RecipeReview r) {
        this.id = r.getId();
        this.username = r.getUser().getName(); // 🔥 Users 엔티티 기준
        this.rating = r.getRating();
        this.content = r.getContent();
        this.createdAt = r.getCreatedAt().toString();
    }
}
