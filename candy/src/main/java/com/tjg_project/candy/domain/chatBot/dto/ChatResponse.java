package com.tjg_project.candy.domain.chatBot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatResponse {
    private String reply;      // 챗봇이 사용자에게 보여줄 메시지
    private Object data;       // 추가 데이터(주문 목록 등)
}
