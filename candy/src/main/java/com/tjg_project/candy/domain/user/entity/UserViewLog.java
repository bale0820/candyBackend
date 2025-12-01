package com.tjg_project.candy.domain.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "user_view_log")
public class UserViewLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long upk; // user pk

    private Long ppk; // product pk

    private Long subCategoryId;

    private LocalDateTime viewedAt = LocalDateTime.now();

    private Integer qty = 1;
}
