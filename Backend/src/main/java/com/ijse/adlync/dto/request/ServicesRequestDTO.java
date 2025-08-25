package com.ijse.adlync.dto.request;

import com.ijse.adlync.entity.enums.ServicesEntityAvailabilityEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServicesRequestDTO {
    private Long service_id;
    private String service_type;
    private String provider_name;
    private ServicesEntityAvailabilityEnum availability;
    private String charges;
    private String service_area;
    private String qualifications;
    private PostRequestDTO postRequestDTO;
}
