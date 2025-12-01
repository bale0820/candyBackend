package com.tjg_project.candy.domain.chatBot.controller;

import com.tjg_project.candy.domain.chatBot.dto.ChatRequest;
import com.tjg_project.candy.domain.chatBot.dto.ChatResponse;
import com.tjg_project.candy.domain.chatBot.service.ChatOrderService;
import com.tjg_project.candy.domain.chatBot.service.IntentService;
import com.tjg_project.candy.domain.order.entity.Order;
import com.tjg_project.candy.domain.order.entity.DeliveryStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/chatbot")
@RequiredArgsConstructor
public class ChatBotController {

    private final IntentService intentService;
    private final ChatOrderService chatOrderService;

    @PostMapping("/ask")
    public ChatResponse ask(@RequestBody ChatRequest req) {

        String intent = intentService.classify(req.getMessage());
        System.out.println("req"+req.getUpk());

        switch(intent) {

            case "ORDER_STATUS":
                return handleOrderStatus(req.getUpk());

            case "DELIVERY_TIME":
                return handleDeliveryTime(req.getUpk());

            case "RETURN_REQUEST":
                return handleReturn(req.getUpk());

            case "RECOMMENDATION":
                return new ChatResponse("고객님께 맞는 상품을 추천해드릴게요!", null);

            default:
                return new ChatResponse("죄송해요, 무슨 말인지 잘 이해하지 못했어요 😥", null);
        }
    }

    // ===============================
    // 1) 주문 조회
    // ===============================
    private ChatResponse handleOrderStatus(Long upk) {

        Order orders = chatOrderService.getLatestOrder(upk);

        if (orders == null)
            return new ChatResponse("고객님의 주문 내역이 없습니다.", null);

        return new ChatResponse("고객님의 주문 내역입니다.", orders);
    }

    // ===============================
    // 2) 배송 시간 안내
    // ===============================
    private ChatResponse handleDeliveryTime(Long upk) {

        Order order = chatOrderService.getLatestOrder(upk);

        if (order == null)
            return new ChatResponse("최근 주문을 찾을 수 없어요.", null);

        if (order.getDeliveryStatus() == DeliveryStatus.DELIVERED)
            return new ChatResponse("이미 배송이 완료된 주문입니다!", order);

        // 예: 배송중이면 도착 예정일 계산
        LocalDateTime eta = order.getOdate().plusDays(2);

        String dateStr = eta.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        return new ChatResponse("현재 배송 중입니다! 예상 도착 시간: " + dateStr, order);
    }

    // ===============================
    // 3) 반품 요청 처리
    // ===============================
    private ChatResponse handleReturn(Long upk) {

        Order order = chatOrderService.getLatestOrder(upk);

        if (order == null)
            return new ChatResponse("반품 가능한 주문이 없습니다.", null);

        // 실제 반품 DB테이블 만들면 여기에 insert
        return new ChatResponse("가장 최근 주문(" + order.getOrderCode() + ")의 반품 요청이 접수되었습니다.", order);
    }
}
