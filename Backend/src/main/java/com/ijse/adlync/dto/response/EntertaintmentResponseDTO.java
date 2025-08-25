package com.ijse.adlync.dto.response;


import com.ijse.adlync.entity.enums.EntertainmentItemConditionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EntertaintmentResponseDTO {
    private Long id;
    private String type;
    private String format;
    private String brand;
    private String genre;
    private Date release_year;
    private String rating;
    private String creator;
    private EntertainmentItemConditionEnum condition;
    private PostResponseDTO postResponseDTO;
}
