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

    private Long animal_id;
    private String species;
    private String breed;
    private Integer age;
    private String gender;
    private String vaccination_status;
    private Long post_id;
}

