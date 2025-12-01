package com.tjg_project.candy.domain.recipe.repository;

import com.tjg_project.candy.domain.recipe.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findBySubCategoryId(Long subId);
}
