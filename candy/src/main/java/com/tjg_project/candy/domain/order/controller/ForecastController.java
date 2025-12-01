package com.tjg_project.candy.domain.order.controller;


import com.tjg_project.candy.domain.order.dto.DailySalesDto;
import com.tjg_project.candy.domain.order.dto.ForecastResponseDto;
import com.tjg_project.candy.domain.order.service.ForecastService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/forecast")
public class ForecastController {

    private final ForecastService forecastService;

    @GetMapping("/sales/{ppk}")
    public List<DailySalesDto> getSales(@PathVariable Long ppk) {
        return forecastService.getDailySales(ppk);
    }

    @GetMapping("/predict/{ppk}")
    public ForecastResponseDto runForecast(@PathVariable Long ppk) {
        try {
            return forecastService.runForecast(ppk);
        } catch (Exception e) {
            throw new RuntimeException("AI 예측 실패: " + e.getMessage());
        }
    }
}