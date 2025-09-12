package com.ijse.adlync.dto.response;

import com.ijse.adlync.entity.enums.PostEntityStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDTO {
    private Long post_id;
    private PostEntityStatusEnum status;
    private String title;
    private String description;
    private String contact_number;
    private double price;
    private Long category_id;
    private Advertisement_typeResponseDTO advertisement_type;
    private Long location_id;
    private Long message_id;
    private UserResponseDTO user;
    private CategoryResponseDTO category;
    private LocationResponseDTO location;
    private PaymentResponseDTO payment;
    private LocalDateTime createdAt;
    private CommonPostResponseDTO common;
    private List<ImageResponseDTO> images;
}
