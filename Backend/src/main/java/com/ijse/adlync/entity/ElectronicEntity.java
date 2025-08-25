package com.ijse.adlync.entity;

import com.ijse.adlync.entity.enums.ElectronicEntityConditionEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
    @Column(name = "`condition`")
    private ElectronicEntityConditionEnum condition;

    @ElementCollection
    private List<String> accessories = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "post_id", nullable = false, unique = true) // owns FK
    private PostEntity post;
}
