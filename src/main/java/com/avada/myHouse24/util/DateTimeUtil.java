package com.avada.myHouse24.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class DateTimeUtil {

    public static Date combineDateTime(Date date, Date time) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalTime localTime = time.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();

        LocalDateTime combinedDateTime = LocalDateTime.of(localDate, localTime);

        return Date.from(combinedDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
