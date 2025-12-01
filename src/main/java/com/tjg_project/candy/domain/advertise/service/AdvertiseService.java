package com.tjg_project.candy.domain.advertise.service;

import com.tjg_project.candy.domain.advertise.entity.Advertise;
import com.tjg_project.candy.domain.advertise.repository.AdvertiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdvertiseService {

    private final AdvertiseRepository advertiseRepository;

    public List<Advertise> getAllAdvertises() {
        return advertiseRepository.findAll();
    }
}
