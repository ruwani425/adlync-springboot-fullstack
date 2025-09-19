package com.ijse.adlync.entity;

import com.ijse.adlync.entity.enums.PostEntityStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "posts")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long post_id;

    //    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PostEntityStatusEnum status;

    //    @Column(nullable = false)
    private String title;

    //    @Column(nullable = false)
    private String description;

    //    @Column(nullable = false)
    private String contact_number;

    private double price;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PaymentEntity payment;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ReportEntity> reports = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advertisement_type_id")
    private Advertisement_typeEntity advertisement_type;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    private LocationEntity location;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MessageEntity> messages = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL)
    private VehicleEntity vehicle;

    @OneToOne(mappedBy = "post")
    private AnimalEntity animal;

    @OneToOne(mappedBy = "post")
    private PropertyEntity property;

    @OneToOne(mappedBy = "post")
    private JobEntity job;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL)
    private MobileEntity mobile;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL)
    private ElectronicEntity electronic;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL)
    private EntertaintmentEntity entertainment;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL)
    private EducationEntity education;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL)
    private ServicesEntity services;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL)
    private AgricultureEntity agriculture;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL)
    private Fashion_and_beautyEntity fashion_and_beauty;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL)
    private KidsEntity kids;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL)
    private EssentialsEntity essentials;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL)
    private SportEntity sport;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL)
    private Work_over_seasEntity work_over_seas;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL)
    private Home_and_gardenEntity home_and_garden;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ImageEntity> images = new ArrayList<>();

    // NEW: Relationship to reviews
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ReviewEntity> reviews = new ArrayList<>();
}