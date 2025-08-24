package com.ijse.adlync.entity;

import com.ijse.adlync.entity.enums.ServicesEntityAvailabilityEnum;
import jakarta.persistence.*;

@Entity
public class ServicesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long service_id;

    private String service_type;

    private String provider_name;

    @Enumerated(EnumType.STRING)
    private ServicesEntityAvailabilityEnum availability;

    private String charges;

    @OneToOne(mappedBy = "services")
    private PostEntity post;

    public Long getService_id() {
        return service_id;
    }

    public void setService_id(Long service_id) {
        this.service_id = service_id;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getProvider_name() {
        return provider_name;
    }

    public void setProvider_name(String provider_name) {
        this.provider_name = provider_name;
    }

    public ServicesEntityAvailabilityEnum getAvailability() {
        return availability;
    }

    public void setAvailability(ServicesEntityAvailabilityEnum availability) {
        this.availability = availability;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }

    public PostEntity getPost() {
        return post;
    }

    public void setPost(PostEntity post) {
        this.post = post;
    }

}
