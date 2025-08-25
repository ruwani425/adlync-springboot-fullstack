package com.ijse.adlync.dto.request;


import com.ijse.adlync.entity.enums.ElectronicEntityConditionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ElectronicRequestDTO {
    private Long electronic_id;
    private String brand;
    private String type;
    private String model;
    private String warranty;
    private ElectronicEntityConditionEnum condition;
    private ArrayList<String> accessories;
    private PostRequestDTO postRequestDTO;
}
