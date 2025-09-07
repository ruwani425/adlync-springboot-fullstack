package com.ijse.adlync.dto.request;

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
    private String availability;
    private String charges;
    private String service_area;
    private String qualifications;
    private PostRequestDTO postRequestDTO;
}
