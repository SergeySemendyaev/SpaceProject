package com.space.model;

import org.springframework.context.annotation.Bean;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ship")
public class ShipModel {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "planet")
    private String planet;

    @Column(name = "shipType")
    @Enumerated(EnumType.STRING)
    private ShipType shipType;

    @Column(name = "prodDate")
    private Date prodDate;

    @Column(name = "isUsed")
    private Boolean isUsed;

    @Column(name = "speed")
    private Double speed;

    @Column(name = "crewSize")
    private Integer crewSize;

    @Column(name = "rating")
    private Double rating;
}
