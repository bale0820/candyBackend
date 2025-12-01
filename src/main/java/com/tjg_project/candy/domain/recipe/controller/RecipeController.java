package com.tjg_project.candy.domain.recipe.controller;

import com.tjg_project.candy.domain.recipe.dto.RecipeReviewRequestDto;
import com.tjg_project.candy.domain.recipe.entity.RecipeReview;
import com.tjg_project.candy.domain.recipe.service.RecipeService;
import com.tjg_project.candy.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/recipe")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;
    private final JwtUtil jwtUtil;

    @GetMapping("/list")
    public ResponseEntity<?> getRecipeList(@RequestParam Long subId) {
        return ResponseEntity.ok(recipeService.getRecipeList(subId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRecipeDetail(@PathVariable Long id) {
        return ResponseEntity.ok(recipeService.getRecipeDetail(id));
    }

    @PostMapping("/{id}/review")
    public ResponseEntity<?> writeReview(
            @PathVariable Long id,
            @RequestBody RecipeReviewRequestDto dto,
            @RequestHeader("Authorization") String authHeader
    ) {
        // 🔥 토큰에서 Bearer 제거
        String token = authHeader.substring(7);
        System.out.println("token"+token);
        // 🔥 JwtUtil 사용해서 유저 ID 파싱
        Long userId = jwtUtil.extractUserId(token);

        // 🔥 저장 로직 호출
        RecipeReview recipeReview = recipeService.saveReview(id, userId, dto);

        return ResponseEntity.ok(Map.of("recipeReview", recipeReview));
    }
}
