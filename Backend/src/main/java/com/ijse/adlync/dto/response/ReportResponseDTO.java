package com.ijse.adlync.dto.response;

import com.ijse.adlync.entity.enums.ReportStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponseDTO {

    private Long report_id;
    private String reason;
    private String customReason;
    private String description;
    private String reporterContact;
    private Boolean anonymous;
    private Long postId;
    private String postTitle;
    private String reporterName;
    private LocalDateTime date;
    private ReportStatusEnum status;
}
