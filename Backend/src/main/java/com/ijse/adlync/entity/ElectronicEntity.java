package com.ijse.adlync.entity;

import com.ijse.adlync.entity.enums.ElectronicEntityConditionEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ElectronicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long electronic_id;

    private String brand;

    private String type;

    private String model;

    private String warranty;

    @Enumerated(EnumType.STRING)
    private ElectronicEntityConditionEnum condition;

    private ArrayList<String> accessories;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    private PostEntity post;
}
