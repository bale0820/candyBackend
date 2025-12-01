package com.tjg_project.candy.domain.order.controller;

import com.tjg_project.candy.domain.order.entity.Order;
import com.tjg_project.candy.domain.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private  OrderService orderService;

    @GetMapping("/my/{id}")
    public ResponseEntity<List<Order>> getMyOrders(@PathVariable Long id) {
        List<Order> orders = orderService.getOrdersByUser(id);
        return ResponseEntity.ok(orders);
    }
    /** 🔹 5) 주문내역 삭제  */
    @DeleteMapping("/deleteOrder/{userId}/{orderCode}")
    public ResponseEntity<?> deleteOrder(
            @PathVariable Long userId,
            @PathVariable String orderCode) {

        boolean deleted = orderService.deleteOrder(userId, orderCode);

        if(deleted) return ResponseEntity.ok("deleted");
        else return ResponseEntity.status(404).body("not found");
    }
}
