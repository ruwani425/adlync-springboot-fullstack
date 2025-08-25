package com.ijse.adlync.dto.response;

import com.ijse.adlync.entity.enums.AgricultureEntityConditionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AgricultureResponseDTO {
    private Long agriculture_id;
    private String product_type;
    private Integer quantity;
    private String season;
    private String variety;
    private Date production_Date;
    private String certifications;
    private AgricultureEntityConditionEnum condition;
    private PostResponseDTO postResponseDTO;
}
