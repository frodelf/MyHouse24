package com.avada.myHouse24.model;

import com.avada.myHouse24.entity.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.util.List;

@Data
public class InvoiceDto {
    private Long id;
    private String number;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date toDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fromDate;
    private Flat flat;
    private User user;
    private Score score;
    private Boolean addToStats;
    private String status;
    private Tariff tariff;
    private List<InvoiceAdditionalService> invoiceAdditionalServices;
}
