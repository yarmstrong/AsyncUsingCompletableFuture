package com.holkem;

public class Discount {

    public enum Code {
        NONE(0), SILVER(5), GOLD(10), PLATINUM(15), DIAMOND(20);
        private final int percentage;
        Code(int percentage) {
            this.percentage = percentage;
        }
    }

    public static String applyDiscount(Quote quote) {
        return quote.getShop() + " price is: " + Util.format(apply(quote.getProductPrice(), quote.getCode()));
    }

    public static double apply(double price, Discount.Code code) {
        return price * (100 - code.percentage) / 100;
    }

}
