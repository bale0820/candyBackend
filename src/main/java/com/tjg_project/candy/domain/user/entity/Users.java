package com.tjg_project.candy.domain.user.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    @Column(length = 5, nullable = true)
    private String role;

    @Column(name = "user_id", length = 50, nullable = false, unique = true)
    private String userId; // 로그인 아이디

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 255, nullable = false, unique = true)
    private String email;

    @Column(length = 255, nullable = false)
    private String password;

    @Column(length = 20)
    private String phone;

    @Column(length = 1)
    private String gender;

    @Column(length = 20)
    private String birthday;

    @Column(length = 255)
    private String address;

    @Column(length = 20)
    private String provider = "local";

    @Column(length = 100)
    private String recommendation;

    @Column(length = 5)
    private String zonecode;
}
