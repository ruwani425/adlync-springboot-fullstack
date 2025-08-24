package com.ijse.adlync.dto.response;

import com.ijse.adlync.entity.enums.CategoryEntityNameEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseDTO {

    private Long category_id;
    private CategoryEntityNameEnum name;
}
