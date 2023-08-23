package com.avada.myHouse24.model;

import com.avada.myHouse24.entity.AdditionalService;
import com.avada.myHouse24.entity.Flat;
import lombok.Data;
import java.sql.Date;


@Data
public class CounterDataDTO {
    private Long id;
    private String number;
    private Date fromDate;
    private String status;
    private String data;
    private Flat flat;
    private AdditionalService additionalService;
}