package com.holkem;

import java.util.Random;

public class Util {
    Random random = new Random();

    static String format(double d) {
        return String.format("%.2f", d);
    }

    static void delay() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
