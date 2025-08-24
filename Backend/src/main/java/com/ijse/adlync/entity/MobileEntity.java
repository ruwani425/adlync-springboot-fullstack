package com.ijse.adlync.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MobileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long mobile_id;

    private String storage;

    private String condition;

    private String warranty_status;

    private String ram;

    private String brand;

    private String model;

    private String colour;

    private String included_accessories;

    private String additional_information;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    private PostEntity post;

}
