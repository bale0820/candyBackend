package com.tjg_project.candy.domain.recipe.service;

import com.tjg_project.candy.domain.recipe.dto.RecipeDetailResponseDto;
import com.tjg_project.candy.domain.recipe.dto.RecipeResponseDto;
import com.tjg_project.candy.domain.recipe.dto.RecipeReviewRequestDto;
import com.tjg_project.candy.domain.recipe.entity.Recipe;
import com.tjg_project.candy.domain.recipe.entity.RecipeReview;
import com.tjg_project.candy.domain.recipe.repository.RecipeRepository;
import com.tjg_project.candy.domain.recipe.repository.RecipeReviewRepository;
import com.tjg_project.candy.domain.user.entity.Users;
import com.tjg_project.candy.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeReviewRepository recipeReviewRepository;
    private final UserRepository userRepository;
    public List<RecipeResponseDto> getRecipeList(Long subId) {
        List<Recipe> list = recipeRepository.findBySubCategoryId(subId);
        return list.stream()
                .map(RecipeResponseDto::new)
                .collect(Collectors.toList());
    }

    public RecipeDetailResponseDto getRecipeDetail(Long id) {
        Recipe r = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        // 후기 가져오기
        List<RecipeReview> reviews = recipeReviewRepository.findByRecipeId(id);

        return new RecipeDetailResponseDto(r, reviews);
    }

    public RecipeReview saveReview(Long recipeId, Long userId, RecipeReviewRequestDto dto) {

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("레시피가 존재하지 않습니다."));

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다."));

        RecipeReview review = RecipeReview.builder()
                .recipe(recipe)
                .user(user)
                .rating(dto.getRating())
                .content(dto.getContent())
                .build();

        return recipeReviewRepository.save(review);
    }
}

