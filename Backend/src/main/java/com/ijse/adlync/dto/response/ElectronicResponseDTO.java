package com.ijse.adlync.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ElectronicResponseDTO {
    private Long electronic_id;
    private String brand;
    private String type;
    private String model;
    private String warranty;
    private String condition;
    private ArrayList<String> accessories;
    private Long post_id;
}
