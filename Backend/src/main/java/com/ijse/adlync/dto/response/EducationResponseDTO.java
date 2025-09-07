package com.ijse.adlync.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EducationResponseDTO {
    private Long education_id;
    private String course_name;
    private String institute;
    private String duration;
    private String qualification_offered;
    private String subject_area;
    private String study_mode;
    private String education_level;
    private String schedule;
    private String requirements;
    private PostResponseDTO postResponseDTO;
}
