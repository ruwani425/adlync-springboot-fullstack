package com.ijse.adlync.dto.request;

import com.ijse.adlync.entity.enums.AnimalEntityGenderEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnimalRequestDTO {

    private String species;
    private String breed;
    private Integer age;
    private AnimalEntityGenderEnum gender;
    private String vaccination_status;
    private PostRequestDTO postRequestDTO;
}

