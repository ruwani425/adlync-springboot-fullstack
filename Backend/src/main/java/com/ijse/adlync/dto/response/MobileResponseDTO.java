package com.ijse.adlync.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MobileResponseDTO {
    private Long mobile_id;
    private String storage;
    private String condition;
    private String warranty_status;
    private String ram;
    private String brand;
    private String model;
    private String colour;
    private String included_accessories;
    private String additional_information;
    private PostResponseDTO postResponseDTO;
}
