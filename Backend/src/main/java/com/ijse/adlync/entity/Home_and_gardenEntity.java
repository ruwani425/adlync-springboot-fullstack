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
public class Home_and_gardenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long home_garden_id;

    private String item_type;

    private String material;

    private String dimensions;

    @Column(name = "`condition`")
    private String condition;

    private String brand;

    private String color;

    private String weight;

    private String assembly_required;

    private String special_features;

    @OneToOne
    @JoinColumn(name = "post_id", referencedColumnName = "post_id")
    private PostEntity post;
}
