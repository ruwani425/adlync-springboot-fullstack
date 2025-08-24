package com.ijse.adlync.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Work_over_seasRequestDTO {
    private Long work_over_seas_id;
    private String position;
    private String country;
    private String salary;
    private String contract_duration;
    private String requirements;
    private String company_name;
    private String visa_status;
    private String accommodation;
    private String benefits;
    private Long post_id;
}
