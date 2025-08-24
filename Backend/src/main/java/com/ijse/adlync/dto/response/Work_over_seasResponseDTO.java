package com.ijse.adlync.dto.response;


public class Work_over_seasResponseDTO {

    private Long overseas_id;
    private String position;
    private String country;
    private String salary;
    private String requirements;
    private String contract_duration;

    public Long getOverseas_id() {
        return overseas_id;
    }

    public void setOverseas_id(Long overseas_id) {
        this.overseas_id = overseas_id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getContract_duration() {
        return contract_duration;
    }

    public void setContract_duration(String contract_duration) {
        this.contract_duration = contract_duration;
    }
}
