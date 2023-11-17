package com.avada.myHouse24.model;

import com.avada.myHouse24.entity.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.util.List;

@Data
public class InvoiceDto {
    private Long id;
    @Length(max = 10)
    private String number;
    private Date date;
    private Date toDate;
    private Date fromDate;
    private Flat flat;
    private User user;
    private Score score;
    private Boolean addToStats;
    private String status;
    private String months;
    private Tariff tariff;
    private Double sum;
    private List<InvoiceAdditionalService> invoiceAdditionalServices;
}
