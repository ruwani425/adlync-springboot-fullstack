package com.ijse.adlync.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Home_and_gardenResponseDTO {
    private Long home_garden_id;
    private String item_type;
    private String material;
    private String dimensions;
    private String condition;
    private String brand;
    private String color;
    private String weight;
    private String assembly_required;
    private String special_features;
    private Long post_id;
}
