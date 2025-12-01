package com.tjg_project.candy.domain.advertise.repository;

import com.tjg_project.candy.domain.advertise.entity.Advertise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvertiseRepository extends JpaRepository<Advertise, Long> {
}
