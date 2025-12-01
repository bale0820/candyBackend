package com.tjg_project.candy.domain.recipe.dto;

import com.tjg_project.candy.domain.recipe.entity.Recipe;
import com.tjg_project.candy.domain.recipe.entity.RecipeReview;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class RecipeDetailResponseDto {

    private RecipeResponseDto recipe;
    private List<ReviewResponseDto> reviews;

    public RecipeDetailResponseDto(Recipe r, List<RecipeReview> reviewList) {
        this.recipe = new RecipeResponseDto(r);
        this.reviews = reviewList.stream()
                .map(ReviewResponseDto::new)
                .collect(Collectors.toList());
    }
}
