package com.avada.myHouse24.model;

import com.avada.myHouse24.services.impl.HouseServiceImpl;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Data
public class CounterDataFilterDto {
    private Long id;
    private Long house;
    private String houseName;
    private Long section;
    private String sectionName;
    private Long flat;
    private String flatName;
    private Long additionalService;
    private String additionalServiceName;

}
