package com.ijse.adlync.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "images")
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long image_id;

    @Column(nullable = false, length = 500)
    private String image_url;

    @Column(length = 255)
    private String image_name;

    @Column(length = 100)
    private String image_type; // e.g., "image/jpeg", "image/png"

    @Column
    private Long image_size; // size in bytes

    @Column(name = "upload_date")
    private LocalDateTime upload_date;

    @Column(name = "is_primary")
    private Boolean is_primary = false; // to mark the main/featured image

    @Column(name = "display_order")
    private Integer display_order; // order to display images

    // Many images belong to one post
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;

    // Constructors
    public ImageEntity() {
        this.upload_date = LocalDateTime.now();
    }

    // Utility methods
    @PrePersist
    protected void onCreate() {
        if (upload_date == null) {
            upload_date = LocalDateTime.now();
        }
    }

    @Override
    public String toString() {
        return "ImageEntity{" +
                "image_id=" + image_id +
                ", image_url='" + image_url + '\'' +
                ", image_name='" + image_name + '\'' +
                ", image_type='" + image_type + '\'' +
                ", image_size=" + image_size +
                ", upload_date=" + upload_date +
                ", is_primary=" + is_primary +
                ", display_order=" + display_order +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageEntity that = (ImageEntity) o;
        return image_id != null ? image_id.equals(that.image_id) : that.image_id == null;
    }

    @Override
    public int hashCode() {
        return image_id != null ? image_id.hashCode() : 0;
    }
}