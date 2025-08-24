package com.ijse.adlync.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long sport_id;

    private String equipment_type;

    private String brand;

    private String condition;

    private String size;

    private String additional_information;

    @OneToOne(mappedBy = "sport")
    private PostEntity post;

}
