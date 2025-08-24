package com.ijse.adlync.dto.request;

import com.ijse.adlync.entity.enums.JobEntityEmployment_typeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobRequestDTO {
    private Long job_id;
    private String position;
    private String company;
    private Double salary_min;
    private Double salary_max;
    private String industry;
    private String job_type;
    private String requirements;
    private String expiriance_level;
    private Long post_id;
}
