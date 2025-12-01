package com.tjg_project.candy.domain.advertise.controller;

import com.tjg_project.candy.domain.advertise.entity.Advertise;
import com.tjg_project.candy.domain.advertise.service.AdvertiseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/advertise")
public class AdvertiseController {

    private final AdvertiseService advertiseService;

    @GetMapping("/list")
    public List<Advertise> getAdvertiseList() {
        return advertiseService.getAllAdvertises();
    }
}

