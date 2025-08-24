package com.ijse.adlync.entity;

import com.ijse.adlync.entity.enums.Fashion_and_beautyEntityGenderEnum;
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
public class Fashion_and_beautyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long fashion_id;

    private String item_type;

    private String brand;

    private String size;

    @Enumerated(EnumType.STRING)
    private Fashion_and_beautyEntityGenderEnum gender;

    private String condition;

    private String color;

    private String material;

    private String style_note;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    private PostEntity post;
}
