package com.ijse.adlync.dto.request;

import com.ijse.adlync.entity.enums.CategoryEntityNameEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDTO {

    private CategoryEntityNameEnum name;
}
