package com.avada.myHouse24.enums;

import java.util.Arrays;
import java.util.List;

public enum InvoiceStatus {
    Paid("Оплачено"),
    Partially_paid("Частично оплачено"),
    Not_paid("Не оплачено");
    private String name;
    InvoiceStatus(String name) {
        this.name = name;
    }
    public static List<String> getAll() {
        return Arrays.asList(Not_paid.name, Paid.name, Partially_paid.name);
    }
}
