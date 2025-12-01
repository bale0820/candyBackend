package com.tjg_project.candy.domain.order.dto;

import lombok.Data;

@Data
public class KakaoReadyResponse {
    private String tid; // 결제 고유 번호
    private String next_redirect_pc_url;  //QR CODE ADDRESS
}


