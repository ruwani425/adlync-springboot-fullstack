package com.ijse.adlync.dto.request;

import com.ijse.adlync.entity.enums.PostEntityStatusEnum;

public class PostRequestDTO {

    private PostEntityStatusEnum status;
    private String title;
    private String description;

    public PostEntityStatusEnum getStatus() {
        return status;
    }

    public void setStatus(PostEntityStatusEnum status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
