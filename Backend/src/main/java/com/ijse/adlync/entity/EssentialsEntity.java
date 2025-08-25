package com.ijse.adlync.entity;

import com.ijse.adlync.entity.enums.EssentialItemConditionEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EssentialsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long essential_id;

    private String brand;

    private Integer quantity;

    private Date expiry_date;

    private String product_type;

    private String storage_instructions;

    @Enumerated(EnumType.STRING)
    @Column(name = "`condition`")
    private EssentialItemConditionEnum condition;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    private PostEntity post;
}
