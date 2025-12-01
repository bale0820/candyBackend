package com.tjg_project.candy.domain.delivery.service;


import com.tjg_project.candy.domain.delivery.entity.Delivery;
import com.tjg_project.candy.domain.delivery.repository.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Override
    public List<Delivery> getDeliveryList() {
        return deliveryRepository.findAll();
    }

}
