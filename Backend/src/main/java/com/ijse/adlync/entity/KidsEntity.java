package com.ijse.adlync.entity;

import com.ijse.adlync.entity.enums.KidsGenderEnum;
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
public class KidsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long kids_id;

    private String item_type;

    private String age_range;

    private String brand;

    @Column(name = "`condition`")
    private String condition;

    private String size;

    private KidsGenderEnum gender;

    private String safety_information;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id",referencedColumnName = "post_id")
    private PostEntity post;
}
