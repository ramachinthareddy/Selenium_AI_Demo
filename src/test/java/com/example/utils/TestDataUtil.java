package com.example.utils;

import java.util.Random;

// Utility to generate test strings (emails, long passwords etc.)
public class TestDataUtil {
    public static String uniqueEmail(String prefix) {
        return prefix + System.currentTimeMillis() + "@example.com";
    }

    public static String longString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) sb.append('a');
        return sb.toString();
    }

    public static String randomNumericString(int length) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) sb.append(r.nextInt(10));
        return sb.toString();
    }
}
