package com.ijse.adlync.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EssentialsRequestDTO {
    private Long essential_id;
    private String brand;
    private Integer quantity;
    private Date expiry_date;
    private String product_type;
    private String storage_instructions;
    private String condition;
    private Long post_id;
}
