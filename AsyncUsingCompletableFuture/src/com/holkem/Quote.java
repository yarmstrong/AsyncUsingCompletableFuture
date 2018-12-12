package com.holkem;

public class Quote {
    private String shop;
    private double productPrice;
    private Discount.Code code;

    public Quote(String shop, double productPrice, Discount.Code code) {
        this.shop = shop;
        this.productPrice = productPrice;
        this.code = code;
    }

    public static Quote parse(String s) {
        String[] split = s.split(":");
        String shop = split[0].trim();
        double price = Double.parseDouble(split[1].trim());
        Discount.Code code = Discount.Code.valueOf(split[2].trim());
        return new Quote(shop, price, code);
    }

    public String getShop() {
        return shop;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public Discount.Code getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "shop='" + shop + '\'' +
                ", productPrice=" + productPrice +
                ", code=" + code +
                '}';
    }
}
