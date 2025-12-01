package com.tjg_project.candy.domain.user.controller;

import com.tjg_project.candy.domain.user.service.UserViewLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/view")
public class UserViewLogController {

    private final UserViewLogService userViewLogService;

    // 🔥 최근 본 상품 저장 API
    @PostMapping("/log")
    public ResponseEntity<?> saveLog(@RequestBody Map<String, Long> body) {
        Long upk = body.get("upk");
        Long ppk = body.get("ppk");
        Long subCategoryId = body.get("categorySubId");
        userViewLogService.saveViewLog(upk, ppk, subCategoryId);

        return ResponseEntity.ok("saved");
    }

    // 🔥 추천용 최근 subCategory 반환 API
    @GetMapping("/recent-subcat/{upk}")
    public ResponseEntity<?> getRecentSubCategory(@PathVariable Long upk) {
        Long subCat = userViewLogService.getRecentSubCategory(upk);
        return ResponseEntity.ok(Map.of("recentSubCategory", subCat));
    }
}
