package com.tjg_project.candy.domain.order.service;

import com.tjg_project.candy.domain.order.dto.KakaoApproveResponse;
import com.tjg_project.candy.domain.order.dto.NaverApproveResponse;
import com.tjg_project.candy.domain.order.entity.KakaoPay;
import com.tjg_project.candy.domain.order.entity.NaverPay;
import com.tjg_project.candy.domain.order.entity.Order;

import java.util.List;

public interface OrderService {
    Order saveOrder(NaverApproveResponse approve, NaverPay naverPay);
    Order saveOrder(KakaoApproveResponse approve, KakaoPay kakaoPay);
    List<Order> getOrdersByUser(Long id);
    boolean deleteOrder(Long userId, String orderCode);
}
