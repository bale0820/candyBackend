package com.tjg_project.candy.domain.advertise.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "advertise")
public class Advertise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long advId;

    private String advImageBanner;

    private String advImageInline;

    private String advDetailImage;

    private String advLink;

    private String advName;
}
