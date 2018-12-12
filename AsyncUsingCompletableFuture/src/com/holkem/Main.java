package com.holkem;

import java.util.List;
import java.util.function.Supplier;

public class Main {
    private static BestPriceFinder finder = new BestPriceFinder();

    public static void main(String[] args) {
        System.out.println("Available processors to work with: " + Runtime.getRuntime().availableProcessors() + '\n');

        try {
            // execute("sequential", () -> finder.findPricesSequential("Dummy Product"));
            execute("parallel", () -> finder.findPricesParallel("Dummy Product"));
            execute("completable future", () -> finder.findPricesFuture("Dummy Product"));
            execute("completable future with executor", () -> finder.findPricesFutureEnhanced("Dummy Product"));
            finder.findPricesFutureReactive("Dummy product");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void execute(String processName, Supplier<List<String>> supplier) {
        long start = System.nanoTime();
        System.out.println("Running " + processName + " process...");
        System.out.println(supplier.get());
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println(processName + " done in: " + duration + " msecs\n");
    }
}

/* Test Results */

/* note: prices and discounts are randomly computed
    and is not connecting to an external process or
    API. the process duration is due to the delay
    method added to each execution

   working with 4 processors */

/* Run 1: 5 shops to process
observation: with delay set to 1sec for each process
- sequential runs in only 1 thread so it takes up ~5*1sec
- parallel uses 4 threads and then is waiting for 1 thread
    to be free so it takes ~4*1sec + 1*1sec
- future uses 4 threads also (by default, uses the same pool
    as with the parallel process) and then is waiting for 1
    thread to be free so it takes ~4*1sec + 1*1sec
- need to use future with increased thread pool size

Running sequential process...
[142.07, 171.19, 132.24, 183.09, 156.71]
sequential done in: 5071 msecs

Running parallel process...
[168.08, 128.00, 182.13, 135.96, 161.02]
parallel done in: 2013 msecs

Running completable future process...
[153.85, 131.33, 175.96, 129.56, 179.18]
completable future done in: 2007 msecs

*/


/* Run 2: 5 shops to process
- future with executor set to pool size equal to
the number of shops to query: all process were
running on separate threads so duration is
~5*1sec

Running completable future process...
[156.15, 124.68, 166.10, 122.28, 129.17]
completable future done in: 2010 msecs

Running completable future with executor process...
[129.30, 163.85, 124.25, 151.63, 130.94]
completable future with executor done in: 1006 msecs

 */