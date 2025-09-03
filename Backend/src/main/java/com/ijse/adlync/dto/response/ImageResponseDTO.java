package com.ijse.adlync.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageResponseDTO {
    private Long image_id;
    private String image_url;
    private String image_name;
    private String image_type;
    private Long image_size;
    private LocalDateTime upload_date;
    private Boolean is_primary;
    private Integer display_order;
    private Long post_id;
}
