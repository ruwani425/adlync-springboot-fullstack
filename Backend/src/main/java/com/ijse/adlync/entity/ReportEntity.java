package com.ijse.adlync.entity;

import com.ijse.adlync.entity.enums.ReportStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long report_id;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "custom_reason")
    private String customReason;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "reporter_contact")
    private String reporterContact;

    @Column(name = "anonymous")
    private Boolean anonymous = true;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;

    @Enumerated(EnumType.STRING)
    private ReportStatusEnum status;

    @PrePersist
    public void prePersist() {
        if (date == null) {
            date = LocalDateTime.now();
        }
        if (anonymous == null) {
            anonymous = true;
        }
    }
}
