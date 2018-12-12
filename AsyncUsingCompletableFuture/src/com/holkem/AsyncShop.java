package com.holkem;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class AsyncShop {
    private final Random random;
    private final String name;

    public AsyncShop(String name) {
        this.name = name;
        random = new Random();
    }

    private double calculatePrice(String product) {
        Util.delay();
        return random.nextDouble() * product.charAt(0) + product.charAt(1);
    }

    /* for testing purposes. used with AsyncShopClient to test on the
        CompletableFuture long process and shortcut implementation.
        BestPriceFinder class is the one doing the async call to make
        a query on each shop, thus the async process here will not be
        used in the main process */
    public Future<Double> getPriceAsync(String product) {
        /* version1 : long process
        CompletableFuture<Double> futurePrice = new CompletableFuture<>(); // will contain product price later
        new Thread(() -> { // fork into another thread
            try {
                double price = calculatePrice(product);
                futurePrice.complete(price); // after waiting finally save price into future
            } catch (Exception e) {
                futurePrice.completeExceptionally(e);
            }
        }).start();
        return futurePrice; // return immediately after delegating task in another thread
        */
        /* version2 : concise */
        return CompletableFuture.supplyAsync(() -> calculatePrice(product));
    }
}

