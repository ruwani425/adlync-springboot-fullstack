package com.ijse.adlync.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SportResponseDTO {
    private Long sport_id;
    private String equipment_type;
    private String brand;
    private String condition;
    private String size;
    private String additional_information;
    private Long post_id;
}
