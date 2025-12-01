package com.tjg_project.candy.domain.order.dto;

import java.time.LocalDateTime;

public class DailySalesDto {

    private LocalDateTime dateTime;
    private Long qty;

    public DailySalesDto(LocalDateTime dateTime, Long qty) {
        this.dateTime = dateTime;
        this.qty = qty;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Long getQty() {
        return qty;
    }
}