package com.tjg_project.candy.domain.order.controller;

import com.tjg_project.candy.domain.order.entity.Cart;
import com.tjg_project.candy.domain.order.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // 장바구니 등록
    @PostMapping("/add")
    public Cart addToCart(@RequestBody Cart cart) {
        return cartService.addToCart(cart);
    }

    // 장바구니 가져오기
    @PostMapping("/cartList")
    public List<Cart> cartList(@RequestBody Cart cart) {

        System.out.println("cartopen");
        System.out.println(cart.getUser().getId());
        return cartService.cartList(cart.getUser().getId());
    }

    // qty 업데이트
    @PostMapping("/updateQty")
    public int updateQty(@RequestBody Cart cart) {
        return cartService.updateQty(cart);
    }

    // 장바구니 물품 삭제
    @PostMapping("/deleteItem")
    public int deleteItem(@RequestBody Cart cart) { return cartService.deleteItem(cart.getCid()); }
}
