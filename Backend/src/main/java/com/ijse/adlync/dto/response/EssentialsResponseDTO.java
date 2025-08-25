package com.ijse.adlync.dto.response;

import com.ijse.adlync.entity.enums.EssentialItemConditionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EssentialsResponseDTO {
    private Long essential_id;
    private String brand;
    private Integer quantity;
    private Date expiry_date;
    private String product_type;
    private String storage_instructions;
    private EssentialItemConditionEnum condition;
    private PostResponseDTO postResponseDTO;
}
