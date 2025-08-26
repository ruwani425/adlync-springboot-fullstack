package com.ijse.adlync.entity;

import com.ijse.adlync.entity.enums.PropertyEntityFurnishedEnum;
import com.ijse.adlync.entity.enums.PropertyEntityTypeEnum;
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
public class PropertyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long property_id;

    @Enumerated(EnumType.STRING)
    private PropertyEntityTypeEnum type;

    private Double land_size;

    private String bedroom;

    private String barthroom;

    @Enumerated(EnumType.STRING)
    private PropertyEntityFurnishedEnum furnished;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    private PostEntity post;
}
