package com.avada.myHouse24.util;

public class IdUtil {
    public static String fromIdToString(long id) {
        return String.format("%010d", id);
    }
    public static long fromStringToId(String paddedNumber) {
        return Long.parseLong(paddedNumber.replaceFirst("^0+", ""));
    }
}
