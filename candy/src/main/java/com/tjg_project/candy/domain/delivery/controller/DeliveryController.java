package com.tjg_project.candy.domain.delivery.controller;


import com.tjg_project.candy.domain.delivery.entity.Delivery;
import com.tjg_project.candy.domain.delivery.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/delivery")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    @GetMapping("/deliveryList")
    public List<Delivery> getDeliveryList() {
        return deliveryService.getDeliveryList();
    }

}
