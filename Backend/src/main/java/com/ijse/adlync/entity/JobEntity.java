package com.ijse.adlync.entity;

import com.ijse.adlync.entity.enums.JobEntityEmployment_typeEnum;
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
public class JobEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long job_id;

    private String position;

    private String company;

    private Double salary_min;

    private Double salary_max;

    private String industry;

    @Enumerated(EnumType.STRING)
    private JobEntityEmployment_typeEnum job_type;

    private String requirements;

    private String expiriance_level;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id",referencedColumnName = "post_id")
    private PostEntity post;
}
