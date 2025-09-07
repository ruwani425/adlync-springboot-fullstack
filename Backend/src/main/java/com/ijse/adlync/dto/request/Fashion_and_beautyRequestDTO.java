package com.ijse.adlync.dto.request;

import com.ijse.adlync.entity.enums.Advertisement_typeEntityTypeEnum;
import com.ijse.adlync.entity.enums.Fashion_and_beautyEntityGenderEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Fashion_and_beautyRequestDTO {
    private Long fashion_id;
    private String item_type;
    private String brand;
    private String size;
    private Fashion_and_beautyEntityGenderEnum gender;
    private String condition;
    private String color;
    private String material;
    private String style_note;
    private Advertisement_typeEntityTypeEnum advertisementType;
    private PostRequestDTO postRequestDTO;
}
