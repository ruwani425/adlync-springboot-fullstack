package com.ijse.adlync.dto.request;

import com.ijse.adlync.entity.enums.PaymentEntityPayment_typeEnum;
import com.ijse.adlync.entity.enums.PaymentEntityStatusEnum;
import com.ijse.adlync.entity.enums.PostEntityStatusEnum;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
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
    private Date payment_date;
    private String slip_url;
    private PaymentEntityPayment_typeEnum payment_type;
    private PaymentEntityStatusEnum payment_status;
    private List<ImageRequestDTO> images;
}
