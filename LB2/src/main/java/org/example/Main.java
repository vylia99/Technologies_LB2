package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;
public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }

    private void run() {
        Scanner scanner = new Scanner(System.in);
        double a = 1;
        double b = 9;
        int n; // 100_000_000;
        System.out.println("Кількість кроків = ");
        n = scanner.nextInt();

        int nThreads = 1;
        while (nThreads < 21) {
            double delta = (b - a) / nThreads;
            totalResult = 0;
            long startTime = System.currentTimeMillis();

            ExecutorService executor = Executors.newFixedThreadPool(nThreads);
            List<Future<Double>> futures = new ArrayList<>();

            for (int i = 0; i < nThreads; i++) {
                double start = a + i * delta;
                double end = a + i * delta + delta;
                int steps = n / nThreads;
                Callable<Double> callable = new IntegralCalculator(start, end, steps, this::fun);
                futures.add(executor.submit(callable));
            }

            for (Future<Double> future : futures) {
                try {
                    double result = future.get();
                    totalResult += result;
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            executor.shutdown();

            long finishTime = System.currentTimeMillis();
            System.out.println("Кількість потоків = " + nThreads);
            System.out.println("Result = " + totalResult);
            System.out.println("Time = " + (finishTime - startTime));
            nThreads++;
        }
    }

    private double totalResult;

    public double fun(double x) {
        return 3 * Math.sqrt(x) * (1 + Math.sqrt(x));
    }
}
