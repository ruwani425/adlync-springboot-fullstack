package com.ijse.adlync.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EntertaintmentRequestDTO {
    private Long id;
    private String type;
    private String format;
    private String brand;
    private String genre;
    private Date release_year;
    private String rating;
    private String creator;
    private String condition;
    private Long post_id;
}
