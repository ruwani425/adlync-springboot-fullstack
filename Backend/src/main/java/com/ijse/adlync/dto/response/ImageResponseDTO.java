package com.ijse.adlync.dto.response;

import java.time.LocalDateTime;

/**
 * <b>Header</b>
 * <p>
 * Description
 * </p>
 *
 * @author Ruwani Ranthika
 * @since 2025-08-24
 */
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
