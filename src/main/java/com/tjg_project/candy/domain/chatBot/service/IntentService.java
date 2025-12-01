package com.tjg_project.candy.domain.chatBot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IntentService {

    public String classify(String message) {

        message = message.toLowerCase();

        if (message.contains("주문") || message.contains("내 주문") || message.contains("주문 조회"))
            return "ORDER_STATUS";

        if (message.contains("배송") || message.contains("언제") || message.contains("도착"))
            return "DELIVERY_TIME";

        if (message.contains("반품") || message.contains("취소"))
            return "RETURN_REQUEST";

        if (message.contains("추천"))
            return "RECOMMENDATION";

        return "UNKNOWN";
    }
}
