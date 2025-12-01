package com.tjg_project.candy.domain.analytics.controller;

import com.tjg_project.candy.domain.analytics.dto.ConversionRateDto;
import com.tjg_project.candy.domain.analytics.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/conversion")
    public List<ConversionRateDto> getConversionRates() {
        return analyticsService.getConversionRates();
    }
}
