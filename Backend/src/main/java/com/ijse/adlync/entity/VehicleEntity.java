package com.ijse.adlync.entity;

import com.ijse.adlync.entity.enums.VehicleEntityFuel_typeEnum;
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
public class VehicleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long vehicle_id;

    private String vehicle_type;

    private String mileage;

    private Long year;

    private String brand;

    private String model;

    @Enumerated(EnumType.STRING)
    private VehicleEntityFuel_typeEnum fuel_type;

    private String transmission;

    @Column(name = "`condition`")
    private String condition;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id", referencedColumnName = "post_id")
    private PostEntity post;
}
