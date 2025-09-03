package com.ijse.adlync.dto.request;

import com.ijse.adlync.entity.enums.AnimalEntityGenderEnum;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AnimalRequestDTO {

    private String species;
    private String breed;
    private Integer age;
    private AnimalEntityGenderEnum gender;
    private String vaccination_status;
    private PostRequestDTO postRequestDTO;
}

