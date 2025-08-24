package com.ijse.adlync.dto.request;

import com.ijse.adlync.entity.enums.AgricultureEntityConditionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgricultureRequestDTO {

    private String product_type;

    private Integer quantity;

    private String season;

    private String variety;

    private Date production_Date;

    private String certifications;

    private AgricultureEntityConditionEnum condition;

    private PostRequestDTO postRequestDTO;
}
