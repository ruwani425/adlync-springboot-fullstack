package com.ijse.adlync.entity;

import com.ijse.adlync.entity.enums.PaymentEntityPayment_typeEnum;
import com.ijse.adlync.entity.enums.PaymentEntityStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long payment_id;

    @Column(nullable = false)
    @CreationTimestamp
    private Date payment_date;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentEntityStatusEnum status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentEntityPayment_typeEnum payment_type;

    @Column(nullable = false)
    private Double amount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", unique = true)
    private PostEntity post;

    private String slip_url;
}
