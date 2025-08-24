package com.ijse.adlync.dto.response;

import com.ijse.adlync.entity.enums.ServicesEntityAvailabilityEnum;

public class ServicesResponseDTO {

    private Long service_id;
    private String service_type;
    private String provider_name;
    private ServicesEntityAvailabilityEnum availability;
    private String charges;

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
}
