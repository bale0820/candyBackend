package com.tjg_project.candy.domain.order.dto;

import java.util.List;

public class ForecastResponseDto {

    private List<Integer> next7Days;
    private List<Integer> next30Days;
    private List<Integer> next12Months;
    private List<Integer> next365Days;

    public ForecastResponseDto(
            List<Integer> next7Days,
            List<Integer> next30Days,
            List<Integer> next12Months,
            List<Integer> next365Days
    ) {
        this.next7Days = next7Days;
        this.next30Days = next30Days;
        this.next12Months = next12Months;
        this.next365Days = next365Days;
    }

    public List<Integer> getNext7Days() { return next7Days; }
    public List<Integer> getNext30Days() { return next30Days; }
    public List<Integer> getNext12Months() { return next12Months; }
    public List<Integer> getNext365Days() { return next365Days; }
}
