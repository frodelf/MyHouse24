package com.avada.myHouse24.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    public static String toMonth(String inputDateStr, Locale locale) {
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd", locale);
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("MMMM yyyy", locale);

        try {
            Date date = inputDateFormat.parse(inputDateStr);
            return outputDateFormat.format(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return inputDateStr;
        }
    }
    public static String toMonthForMY(String inputDateStr, Locale locale) {
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM", locale);
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("MMMM yyyy", locale);

        try {
            Date date = inputDateFormat.parse(inputDateStr);
            return outputDateFormat.format(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return inputDateStr; // Якщо формат дати неправильний, повертаємо початковий рядок
        }
    }
}
