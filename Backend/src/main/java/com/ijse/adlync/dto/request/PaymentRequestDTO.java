package com.ijse.adlync.dto.request;

import com.ijse.adlync.entity.enums.PaymentEntityPayment_typeEnum;
import com.ijse.adlync.entity.enums.PaymentEntityStatusEnum;
import java.util.Date;

public class PaymentRequestDTO {

    private Date payment_date;
    private PaymentEntityStatusEnum status;
    private PaymentEntityPayment_typeEnum payment_type;
    private Double amount;

    public Date getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(Date payment_date) {
        this.payment_date = payment_date;
    }

    public PaymentEntityStatusEnum getStatus() {
        return status;
    }

    public void setStatus(PaymentEntityStatusEnum status) {
        this.status = status;
    }

    public PaymentEntityPayment_typeEnum getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(PaymentEntityPayment_typeEnum payment_type) {
        this.payment_type = payment_type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
