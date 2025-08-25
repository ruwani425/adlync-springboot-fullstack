package com.ijse.adlync.dto.response;

import com.ijse.adlync.entity.enums.KidsGenderEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KidsResponseDTO {
    private Long kids_id;
    private String item_type;
    private String age_range;
    private String brand;
    private String condition;
    private String size;
    private KidsGenderEnum gender;
    private String safety_information;
    private PostResponseDTO postResponseDTO;

}
