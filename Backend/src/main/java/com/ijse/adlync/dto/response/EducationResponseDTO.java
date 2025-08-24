package com.ijse.adlync.dto.response;


public class EducationResponseDTO {

    private Long education_id;
    private String course_name;
    private String institute;
    private String duration;
    private String qulification_offered;

    public Long getEducation_id() {
        return education_id;
    }

    public void setEducation_id(Long education_id) {
        this.education_id = education_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getQulification_offered() {
        return qulification_offered;
    }

    public void setQulification_offered(String qulification_offered) {
        this.qulification_offered = qulification_offered;
    }
}
