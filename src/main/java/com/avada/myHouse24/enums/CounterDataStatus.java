package com.avada.myHouse24.enums;

import java.util.Arrays;
import java.util.List;

public enum CounterDataStatus {
    New("Новое"),
    Reviewed("Учтено"),
    ReviewedAndPaid("Учтено и оплачено"),
    Zero("Нулевое");
    private String name;
    CounterDataStatus(String name) {
        this.name = name;
    }
    public static List<String> getAll() {
        return Arrays.asList(New.name, Reviewed.name, ReviewedAndPaid.name, Zero.name);
    }
}
