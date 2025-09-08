package com.ijse.adlync.dto.request;

import com.ijse.adlync.entity.enums.PostEntityStatusEnum;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PostRequestDTO {
    private Long post_id;
    private PostEntityStatusEnum status;
    private String title;
    private String description;
    private String contact_number;
    private double price;
    private String city;
    private String district;
    private String address;
    private LocalDateTime createdAt;
    private List<ImageRequestDTO> images;
}
