package com.ijse.adlync.dto.response;

import com.ijse.adlync.entity.enums.PropertyEntityFurnishedEnum;
import com.ijse.adlync.entity.enums.PropertyEntityTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PropertyResponseDTO {
    private Long property_id;
    private String type;
    private Double land_size;
    private String bedroom;
    private String barthroom;
    private String furnished;
    private Long post_id;
}
