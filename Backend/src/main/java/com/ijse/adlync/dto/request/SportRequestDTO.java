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
public class SportRequestDTO {
    private Long sport_id;
    private String equipment_type;
    private String brand;
    private String condition;
    private String size;
    private String additional_information;
    private Advertisement_typeEntityTypeEnum advertisementType;
    private PostRequestDTO postRequestDTO;
}
