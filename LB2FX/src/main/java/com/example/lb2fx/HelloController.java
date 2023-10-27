package com.example.lb2fx;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.DoubleUnaryOperator;
import java.util.stream.IntStream;

public class HelloController {

    @FXML
    private TextArea n;
    @FXML
    private TextArea potok;
    @FXML
    private TextArea time;

    @FXML
    private TextArea result;
    @FXML
    private Button cal;

    private double totalResult;

    @FXML
    public void calculate() {
        int pn = Integer.parseInt(n.getText());
        int pp = Integer.parseInt(potok.getText());
        Task<Double> calculationTask = new Task<>() {
            @Override
            protected Double call() throws Exception {
                return calculateResult(pn, pp);
            }
        };

        calculationTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                double res = calculationTask.getValue();
                result.setText(String.valueOf(res));

            }
        });

        new Thread(calculationTask).start();
    }

    private double calculateResult(int n, int nThreads ) {
        double a = 1;
        double b = 9;

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

        time.setText(String.format("%.3f", (finishTime - startTime) / 1000.0));
        return totalResult;
    }

    private double fun(double x) {
        return 3 * Math.sqrt(x) * (1 + Math.sqrt(x));
    }
}
