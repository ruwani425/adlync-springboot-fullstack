package com.ijse.adlync.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KidsRequestDTO {
    private Long kids_id;
    private String item_type;
    private String age_range;
    private String brand;
    private String condition;
    private String size;
    private String gender;
    private String safety_information;
    private Long post_id;
}
