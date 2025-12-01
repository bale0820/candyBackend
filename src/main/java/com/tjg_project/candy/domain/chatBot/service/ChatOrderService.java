package com.tjg_project.candy.domain.chatBot.service;

import com.tjg_project.candy.domain.order.entity.Order;
import com.tjg_project.candy.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatOrderService {

    private final OrderRepository orderRepository;

    // 특정 유저의 모든 주문 조회
    public List<Order> getOrders(Long upk) {
        return orderRepository.findByUpk(upk);
    }

    public Order getLimit1Orders(Long upk) {return  orderRepository.findTop1ByUpk(upk);}
    // 최근 주문 1개 가져오기
    public Order getLatestOrder(Long upk) {
        return orderRepository.findTop1ByUpkOrderByOdateDesc(upk);
    }
}
