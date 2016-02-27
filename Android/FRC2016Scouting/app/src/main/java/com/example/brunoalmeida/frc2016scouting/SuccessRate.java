package com.example.brunoalmeida.frc2016scouting;

/**
 * Represents a fraction, with a numerator and denominator.
 * Can be represented as a normal fraction or a percent.
 *
 * Created by Bruno on 2016-02-25.
 */
public class SuccessRate {

    private int numerator;
    private int denominator;

    public SuccessRate() {
        this(0, 0);
    }

    public SuccessRate(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public int getNumerator() {
        return numerator;
    }

    public int getDenominator() {
        return denominator;
    }

    public int getPercent() {
        if (denominator == 0) {
            return 0;
        } else {
            return (numerator * 100) / denominator;
        }
    }

    @Override
    public String toString() {
        return numerator + "/" + denominator;
    }

    public String toPercent() {
        return getPercent() + "%";
    }

}
