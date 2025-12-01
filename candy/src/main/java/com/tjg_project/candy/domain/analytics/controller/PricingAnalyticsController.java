package com.tjg_project.candy.domain.analytics.controller;

import com.tjg_project.candy.domain.analytics.dto.ProductPricingStatsDto;
import com.tjg_project.candy.domain.analytics.service.PricingAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/pricing")
@RequiredArgsConstructor
public class PricingAnalyticsController {

    private final PricingAnalyticsService pricingAnalyticsService;

    // 개별 상품 분석
    @GetMapping("/{ppk}")
    public ProductPricingStatsDto getProductStats(@PathVariable Long ppk) {
        return pricingAnalyticsService.getStats(ppk);
    }

    // ⭐ 전체 상품 분석 추가
    @GetMapping("/all")
    public List<ProductPricingStatsDto> getAllProductStats() {
        return pricingAnalyticsService.getAllStats();
    }
}

