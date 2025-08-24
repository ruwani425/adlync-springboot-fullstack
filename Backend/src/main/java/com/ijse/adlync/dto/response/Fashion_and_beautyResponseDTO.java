package com.ijse.adlync.dto.response;

import com.ijse.adlync.entity.enums.Fashion_and_beautyEntityGenderEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Fashion_and_beautyResponseDTO {
    private Long fashion_id;
    private String item_type;
    private String brand;
    private String size;
    private String gender;
    private String condition;
    private String color;
    private String material;
    private String style_note;
    private Long post_id;
}
