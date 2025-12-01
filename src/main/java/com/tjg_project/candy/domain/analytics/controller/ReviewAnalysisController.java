package com.tjg_project.candy.domain.analytics.controller;

import com.tjg_project.candy.domain.analytics.dto.ReviewAnalysisDto;
import com.tjg_project.candy.domain.analytics.service.ReviewAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/reviews")
@RequiredArgsConstructor
public class ReviewAnalysisController {

    private final ReviewAnalysisService reviewAnalysisService;

    @GetMapping("/analysis/{ppk}")
    public ReviewAnalysisDto analyze(@PathVariable Long ppk) {
        return reviewAnalysisService.analyzeReviews(ppk);
    }
}
