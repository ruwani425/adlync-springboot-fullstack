package com.ijse.adlync.entity;

import com.ijse.adlync.entity.enums.Advertisement_typeEntityTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Advertisement_typeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long ad_id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Advertisement_typeEntityTypeEnum type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @OneToMany(mappedBy = "advertisement_type", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostEntity> posts = new ArrayList<>();
}
