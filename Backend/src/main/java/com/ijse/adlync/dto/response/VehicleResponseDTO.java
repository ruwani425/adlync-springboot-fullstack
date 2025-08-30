package com.ijse.adlync.dto.response;

import com.ijse.adlync.entity.enums.VehicleEntityFuel_typeEnum;
import com.ijse.adlync.entity.enums.VehicleParkingEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleResponseDTO {
    private Long vehicle_id;
    private String vehicle_type;
    private String mileage;
    private Date year;
    private String brand;
    private String model;
    private VehicleEntityFuel_typeEnum fuel_type;
    private String transmission;
    private String condition;
    private PostResponseDTO postResponseDTO;
}
