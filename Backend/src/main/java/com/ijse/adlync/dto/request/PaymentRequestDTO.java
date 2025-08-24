package com.ijse.adlync.dto.request;

import com.ijse.adlync.entity.enums.PaymentEntityPayment_typeEnum;
import com.ijse.adlync.entity.enums.PaymentEntityStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDTO {

    private Date payment_date;
    private PaymentEntityStatusEnum status;
    private PaymentEntityPayment_typeEnum payment_type;
    private Double amount;
}
