package com.holkem;

import java.util.Random;

public class Shop {
    private final Random random;
    private final String name;

    public Shop(String name) {
        this.name = name;
        random = new Random();
    }

    public String getName() {
        return name;
    }

    public String getPrice(String product) {
        double price = calculatePrice(product);
        Discount.Code discount = Discount.Code.values()[random.nextInt(Discount.Code.values().length)];
        return getName() + ":" + Util.format(price) + ":" + discount;
    }

    private double calculatePrice(String product) {
        Util.delay();
        // if (true) throw new RuntimeException("product not available");
        return random.nextDouble() * product.charAt(0) + product.charAt(product.length()-1);
    }
}

