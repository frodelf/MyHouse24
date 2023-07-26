package com.avada.myHouse24.util;

import java.util.Random;

public class NumberUtil {
    public static String generateRandomNumber() {
        Random random = new Random();
        long min = 1000000000L;
        long max = 9999999999L;
        return String.valueOf(min + ((long) (random.nextDouble() * (max - min + 1))));
    }
}
