package com.ijse.adlync.dto.response;

import com.ijse.adlync.entity.enums.PaymentEntityPayment_typeEnum;
import com.ijse.adlync.entity.enums.PaymentEntityStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PaymentResponseDTO {

    private Long payment_id;
    private Date payment_date;
    private PaymentEntityStatusEnum status;
    private PaymentEntityPayment_typeEnum payment_type;
    private Double amount;
    private String slip_url;
}
