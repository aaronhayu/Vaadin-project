package com.example.shop.backend.utils;

import java.util.Random;

public class Util {
    public static String generateEightDigitNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(8);

        for (int i = 0; i < 8; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }
}
