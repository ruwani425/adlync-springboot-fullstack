package com.ijse.adlync.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommonPostResponseDTO {
    private AgricultureResponseDTO agriculture;
    private AnimalResponseDTO animal;
    private EducationResponseDTO education;
    private ElectronicResponseDTO electronic;
    private EntertaintmentResponseDTO entertainment;
    private EssentialsResponseDTO essentials;
    private Fashion_and_beautyResponseDTO fashion_and_beauty;
    private Home_and_gardenResponseDTO home_and_garden;
    private JobResponseDTO job;
    private KidsResponseDTO kids;
    private MobileResponseDTO mobile;
    private PropertyResponseDTO property;
    private ServicesResponseDTO services;
    private SportResponseDTO sport;
    private VehicleResponseDTO vehicle;
    private Work_over_seasResponseDTO work_over_seas;
}
