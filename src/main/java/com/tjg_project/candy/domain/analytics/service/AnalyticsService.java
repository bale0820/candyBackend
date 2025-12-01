package com.tjg_project.candy.domain.analytics.service;

import com.tjg_project.candy.domain.analytics.dto.ConversionRateDto;
import com.tjg_project.candy.domain.analytics.repository.AnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final AnalyticsRepository analyticsRepository;

    public List<ConversionRateDto> getConversionRates() {
        return analyticsRepository.getConversionRates();
    }
}
