package com.tjg_project.candy.domain.recipe.dto;

import com.nimbusds.jose.shaded.gson.Gson;
import com.tjg_project.candy.domain.recipe.entity.Recipe;
import lombok.Data;

import java.util.List;

@Data
public class RecipeResponseDto {

    private Long id;
    private String title;
    private String imageUrl;
    private double rating;
    private int reviewCount;

    private Integer cookTime;
    private String difficulty;

    private String summary;

    private List<String> ingredients;
    private List<String> steps;

    private String tips;
    private String youtubeUrl;

    public RecipeResponseDto(Recipe r) {
        this.id = r.getId();
        this.title = r.getTitle();
        this.imageUrl = r.getImageUrl();
        this.rating = r.getRating();
        this.reviewCount = r.getReviewCount();
        this.cookTime = r.getCookTime();
        this.difficulty = r.getDifficulty();
        this.summary = r.getSummary();
        this.tips = r.getTips();
        this.youtubeUrl = r.getYoutubeUrl();
        this.ingredients = new Gson().fromJson(r.getIngredients(), List.class);
        this.steps = new Gson().fromJson(r.getSteps(), List.class);
    }
}
