package com.ijse.adlync.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Work_over_seasResponseDTO {
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
    private PostResponseDTO postResponseDTO;
}
