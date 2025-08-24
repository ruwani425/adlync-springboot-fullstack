package com.ijse.adlync.dto.request;

import com.ijse.adlync.entity.enums.JobEntityEmployment_typeEnum;

public class JobRequestDTO {

    private String position;
    private String company;
    private Double salary;
    private JobEntityEmployment_typeEnum employment_type;
    private String quilifications;
    private String expiriance_required;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public JobEntityEmployment_typeEnum getEmployment_type() {
        return employment_type;
    }

    public void setEmployment_type(JobEntityEmployment_typeEnum employment_type) {
        this.employment_type = employment_type;
    }

    public String getQuilifications() {
        return quilifications;
    }

    public void setQuilifications(String quilifications) {
        this.quilifications = quilifications;
    }

    public String getExpiriance_required() {
        return expiriance_required;
    }

    public void setExpiriance_required(String expiriance_required) {
        this.expiriance_required = expiriance_required;
    }
}
