package com.tjg_project.candy.domain.recipe.repository;

import com.tjg_project.candy.domain.recipe.entity.RecipeReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeReviewRepository extends JpaRepository<RecipeReview, Long> {

    // ⭐ 해당 레시피의 후기들만 가져오기
    List<RecipeReview> findByRecipeId(Long recipeId);

}
