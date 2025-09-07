package com.ijse.adlync.dto.request;


import com.ijse.adlync.entity.enums.Advertisement_typeEntityTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MobileRequestDTO {
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
    private Advertisement_typeEntityTypeEnum advertisementType;
    private PostRequestDTO postRequestDTO;
}
