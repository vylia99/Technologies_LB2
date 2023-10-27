package com.example.lb2fx;
import java.util.concurrent.Callable;
import java.util.function.DoubleUnaryOperator;
import java.util.stream.IntStream;

public class IntegralCalculator implements Callable<Double> {

        private double start;
        private double end;
        private int nSteps;
        private DoubleUnaryOperator f;

        public IntegralCalculator(double start, double end, int nSteps, DoubleUnaryOperator f) {
            this.start = start;
            this.end = end;
            this.nSteps = nSteps;
            this.f = f;
        }

        @Override
        public Double call() {
            DoubleUnaryOperator mapper = f;
            double step = (end - start) / nSteps;
            double sum = (mapper.applyAsDouble(start) + mapper.applyAsDouble(end)) / 2 +
                    2 * mapper.applyAsDouble(end - step / 2);
            sum = sum * step / 3;
            int bound = nSteps;
            sum += IntStream.range(1, bound).mapToDouble(i -> start + i * step).map(v -> mapper.applyAsDouble(v)
                    + 2 * mapper.applyAsDouble(v - step / 2)).map(y -> y * step / 3).sum();
            return sum;
        }
    }


