package com.ijse.adlync.entity;

import com.ijse.adlync.entity.enums.AgricultureEntityConditionEnum;
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
public class AgricultureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long agriculture_id;

    private String product_type;

    private Integer quantity;

    private String season;

    private String variety;

    private Date production_Date;

    private String certifications;

    @Enumerated(EnumType.STRING)
    @Column(name = "`condition`", nullable = false)
    private AgricultureEntityConditionEnum condition;

    @OneToOne
    @JoinColumn(name = "post_id", nullable = false, unique = true)
    private PostEntity post;
}
