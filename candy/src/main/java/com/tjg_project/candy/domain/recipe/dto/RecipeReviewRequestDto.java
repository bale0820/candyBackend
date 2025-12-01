package com.tjg_project.candy.domain.recipe.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeReviewRequestDto {
    private double rating;
    private String content;
}
