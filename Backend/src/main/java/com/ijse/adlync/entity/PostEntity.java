package com.ijse.adlync.entity;

import com.ijse.adlync.entity.enums.PostEntityStatusEnum;
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
@Table(name = "posts")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long post_id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PostEntityStatusEnum status;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String contact_number;

    private double price;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "report_id")
    private ReportEntity report;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PaymentEntity> payments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReportEntity> reports = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advertisement_type_id")
    private Advertisement_typeEntity advertisement_type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private LocationEntity location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id")
    private MessageEntity message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToOne(mappedBy = "post",cascade = CascadeType.ALL,fetch =  FetchType.LAZY)
    private VehicleEntity vehicle;

    @OneToOne(mappedBy = "post")
    private AnimalEntity animal;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "property_id")
    private PropertyEntity property;

    @OneToOne(mappedBy = "post")
    private JobEntity job;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private MobileEntity mobile;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ElectronicEntity electronic;

    @OneToOne(mappedBy = "post")
    private EducationEntity education;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "services_id")
    private ServicesEntity services;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AgricultureEntity agriculture;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Fashion_and_beautyEntity fashion_and_beauty;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private KidsEntity kids;

    @OneToOne(mappedBy = "post")
    private EssentialsEntity essentials;


    @OneToOne(mappedBy = "post",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private SportEntity sport;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "work_over_seas_id")
    private Work_over_seasEntity work_over_seas;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Home_and_gardenEntity home_and_garden;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("display_order ASC, upload_date ASC")
    private List<ImageEntity> images = new ArrayList<>();

    // Utility methods for managing images
    public void addImage(ImageEntity image) {
        this.images.add(image);
        image.setPost(this);
    }

    public void removeImage(ImageEntity image) {
        this.images.remove(image);
        image.setPost(null);
    }

    // Get primary/featured image
    public ImageEntity getPrimaryImage() {
        return images.stream()
                .filter(ImageEntity::getIs_primary)
                .findFirst()
                .orElse(images.isEmpty() ? null : images.get(0));
    }

    // Set primary image
    public void setPrimaryImage(ImageEntity image) {
        // Remove primary flag from all images
        images.forEach(img -> img.setIs_primary(false));
        // Set the new primary image
        if (image != null && images.contains(image)) {
            image.setIs_primary(true);
        }
    }
}
