package com.holkem;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class BestPriceFinder {
    private final List<Shop> shops = List.of(
            new Shop("Shop 1"),
            new Shop("Shop 2"),
            new Shop("Shop 3"),
            new Shop("Shop 4"),
            new Shop("Shop 5"));
    private final int MAX_THREAD_SIZE = 100;
    private final Executor executor = Executors.newFixedThreadPool(Math.min(shops.size(), MAX_THREAD_SIZE), runnableTask -> {
        Thread t = new Thread(runnableTask);
        t.setDaemon(true); // to kill threads upon exit from app
        return t;
    });

    public List<String> findPricesSequential(String product) {
        return shops.stream()
                .map(shop -> shop.getPrice(product))
                .map(Quote::parse)
                .peek(System.out::println)
                .map(Discount::applyDiscount)
                .collect(toList());
    }

    public List<String> findPricesParallel(String product) {
        return shops.parallelStream()
                .map(shop -> shop.getPrice(product))
                .map(Quote::parse)
                .peek(System.out::println)
                .map(Discount::applyDiscount)
                .collect(toList());
    }

    public List<String> findPricesFuture(String product) {
        List<CompletableFuture<String>> futures = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(() -> shop.getPrice(product)))
                .map(future -> future.thenApply(Quote::parse))
                .map(future -> future.thenCompose(quote ->
                        CompletableFuture.supplyAsync(() ->
                                Discount.applyDiscount(quote))
                ))
                .collect(Collectors.toList());
        return futures.stream()
                .map(CompletableFuture::join)
                .collect(toList());
    }

    public List<String> findPricesFutureEnhanced(String product) {
        List<CompletableFuture<String>> futures = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(()
                        -> shop.getPrice(product), executor)
                               .orTimeout(3, TimeUnit.SECONDS))
                .map(future -> future.thenApply(Quote::parse))
                .map(future -> future.thenCompose(quote ->
                        CompletableFuture.supplyAsync(() ->
                                Discount.applyDiscount(quote), executor)
                ))
                .collect(Collectors.toList());
        return futures.stream()
                .map(CompletableFuture::join)
                .collect(toList());
    }

    public void findPricesFutureReactive(String product) {
        /* loop thru each CompletableFuture and provide a consumer action to
            thenAccept() method to execute once future is completed
            */
        long start = System.nanoTime();

        CompletableFuture[] futures = findPricesStream(product)
                .map(future -> future.thenAccept(
                        str -> System.out.println(str + ", done in " + ((System.nanoTime()-start) / 1_000_000) + " msecs")
                ))
                .toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(futures).join();

        System.out.println("All reactive completable futures done in: " + ((System.nanoTime()-start) / 1_000_000) + " msecs");
    }

    private Stream<CompletableFuture<String>> findPricesStream(String product) {
        return shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(()
                        -> shop.getPrice(product), executor))
                .map(future -> future.thenApply(Quote::parse))
                .map(future -> future.thenCompose(quote ->
                        CompletableFuture.supplyAsync(() ->
                                Discount.applyDiscount(quote), executor)
                ));
    }
}
