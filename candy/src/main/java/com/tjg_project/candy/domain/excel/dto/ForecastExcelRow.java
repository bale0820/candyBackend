package com.tjg_project.candy.domain.excel.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ForecastExcelRow {
    private String type;
    private String date; // yyyy-MM-dd
    private Integer value; // 예측값
}
