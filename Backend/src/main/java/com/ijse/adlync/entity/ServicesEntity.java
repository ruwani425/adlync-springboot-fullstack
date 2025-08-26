package com.ijse.adlync.entity;

import com.ijse.adlync.entity.enums.ServicesEntityAvailabilityEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ServicesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long service_id;

    private String service_type;

    private String provider_name;

    @Enumerated(EnumType.STRING)
    private ServicesEntityAvailabilityEnum availability;

    private String charges;

    private String service_area;

    private String qualifications;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    private PostEntity post;
}
