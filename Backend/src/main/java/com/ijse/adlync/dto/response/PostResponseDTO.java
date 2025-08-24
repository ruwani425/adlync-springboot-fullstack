package com.ijse.adlync.dto.response;

import com.ijse.adlync.entity.enums.PostEntityStatusEnum;

public class PostResponseDTO {

    private Long post_id;
    private PostEntityStatusEnum status;
    private String title;
    private String description;

    public Long getPost_id() {
        return post_id;
    }

    public void setPost_id(Long post_id) {
        this.post_id = post_id;
    }

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
