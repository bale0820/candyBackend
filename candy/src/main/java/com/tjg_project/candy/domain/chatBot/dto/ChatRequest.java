package com.tjg_project.candy.domain.chatBot.dto;

import lombok.Data;

@Data
public class ChatRequest {
    private Long upk;       // 사용자 PK
    private String message; // 사용자 메시지
}
