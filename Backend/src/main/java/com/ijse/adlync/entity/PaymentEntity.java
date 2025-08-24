package com.ijse.adlync.entity;

import com.ijse.adlync.entity.enums.PaymentEntityPayment_typeEnum;
import com.ijse.adlync.entity.enums.PaymentEntityStatusEnum;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long payment_id;

    @Column(nullable = false)
    private Date payment_date;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentEntityStatusEnum status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentEntityPayment_typeEnum payment_type;

    @Column(nullable = false)
    private Double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity post;

    public Long getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(Long payment_id) {
        this.payment_id = payment_id;
    }

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

    public PostEntity getPost() {
        return post;
    }

    public void setPost(PostEntity post) {
        this.post = post;
    }

}
