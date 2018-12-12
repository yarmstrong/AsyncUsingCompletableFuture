package com.holkem;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AsyncShopClient {

    public static void main(String[] args) {
        AsyncShop shop = new AsyncShop("BestShop");
        long start = System.nanoTime();

        Future<Double> futurePrice = shop.getPriceAsync("Dummy Product");
        long invocationTime = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("Async call returned after " + invocationTime + " milliseconds");

        doSomethingElse();

        try {
            Double price = futurePrice.get(5, TimeUnit.SECONDS);
            System.out.printf("Retrieved price in async is %.2f%n", price);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        long retrievalTime = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("Price retrieved upon checking is " + retrievalTime + " milliseconds");
    }

    private static void doSomethingElse() {
        System.out.println("Doing something else...");
    }
}
