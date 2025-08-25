package com.ijse.adlync.entity;

import com.ijse.adlync.entity.enums.AnimalEntityGenderEnum;
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
public class AnimalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long animal_id;

    private String species;

    private String breed;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private AnimalEntityGenderEnum gender;

    private String vaccination_status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    private PostEntity post;
}
