package com.ijse.adlync.dto.request;

import com.ijse.adlync.entity.enums.PostEntityStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestDTO {
    private Long post_id;
    private PostEntityStatusEnum status;
    private String title;
    private String description;
    private String contact_number;
    private double price;
    private Long category_id;
    private Long advertisement_type_id;
    private Long location_id;
    private Long message_id;
    private Long user_id;
    private List<ImageRequestDTO> images;
}
