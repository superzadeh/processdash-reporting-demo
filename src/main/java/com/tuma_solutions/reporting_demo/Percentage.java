package com.tuma_solutions.reporting_demo;

public class Percentage {

    private double value;

    protected Percentage(double value) {
        if (Double.isInfinite(value) || Double.isNaN(value))
            this.value = 0;
        else
            this.value = value;
    }

    public double doubleValue() {
        return (double) value;
    }

}
