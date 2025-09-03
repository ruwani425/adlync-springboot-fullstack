package com.ijse.adlync.dto.request;

import com.ijse.adlync.entity.enums.Advertisement_typeEntityTypeEnum;
import com.ijse.adlync.entity.enums.PropertyEntityFurnishedEnum;
import com.ijse.adlync.entity.enums.PropertyEntityTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PropertyRequestDTO {

    private Long property_id;
    private PropertyEntityTypeEnum type;
    private Double land_size;
    private String bedroom;
    private String barthroom;
    private PropertyEntityFurnishedEnum furnished;
    private Advertisement_typeEntityTypeEnum advertisement_type;
    private PostRequestDTO postRequestDTO;
}
