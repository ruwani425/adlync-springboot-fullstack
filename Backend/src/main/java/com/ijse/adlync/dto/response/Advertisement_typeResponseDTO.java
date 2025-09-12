package com.ijse.adlync.dto.response;

import com.ijse.adlync.entity.enums.Advertisement_typeEntityTypeEnum;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Advertisement_typeResponseDTO {

    private Long ad_id;
    private Advertisement_typeEntityTypeEnum type;
}
