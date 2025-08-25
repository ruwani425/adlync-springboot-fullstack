package com.ijse.adlync.entity;

import com.ijse.adlync.entity.enums.EntertainmentItemConditionEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EntertaintmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;

    private String type;

    private String format;

    private String brand;

    private String genre;

    private Date release_year;

    private String rating;

    private String creator;

    @Enumerated(EnumType.STRING)
    @Column(name = "`condition`")
    private EntertainmentItemConditionEnum condition;

    @OneToOne
    @JoinColumn(name = "post_id", nullable = false, unique = true)
    private PostEntity post;
}
