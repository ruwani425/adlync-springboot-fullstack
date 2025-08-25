package com.ijse.adlync.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EducationRequestDTO {
    private Long education_id;
    private String course_name;
    private String institute;
    private String duration;
    private String qulification_offered;
    private String subject_area;
    private String study_mood;
    private String education_level;
    private String schedule;
    private String requirements;
    private PostRequestDTO postRequestDTO;
}
