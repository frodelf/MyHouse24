package com.avada.myHouse24.model;

import lombok.Data;

@Data
public class CounterDataFilterDto {
    private Long id;
    private Long house;
    private Long section;
    private Long flat;
    private Long additionalService;
}
