package com.ijse.adlync.dto.response;

import com.ijse.adlync.entity.enums.AnimalEntityGenderEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnimalResponseDTO {

    private Long animal_id;
    private String species;
    private String breed;
    private Integer age;
    private AnimalEntityGenderEnum gender;
    private String vaccination_status;
    private PostResponseDTO postResponseDTO;
}
