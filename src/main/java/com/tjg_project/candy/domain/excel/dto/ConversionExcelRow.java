package com.tjg_project.candy.domain.excel.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ConversionExcelRow {
    private String productName;
    private Integer clicks;
    private Integer orders;
    private Double conversionRate;
}
