package com.avada.myHouse24.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class HouseForViewDto {
    private String id;
    @Length(max = 50)
    private String name;
    @Length(max = 50)
    private String address;
}
