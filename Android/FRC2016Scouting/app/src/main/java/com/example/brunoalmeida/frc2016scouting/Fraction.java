package com.example.brunoalmeida.frc2016scouting;

/**
 * Represents a fraction, with a numerator and denominator.
 * Can be represented as a normal fraction or a percent.
 *
 * Created by Bruno on 2016-02-25.
 */
public class Fraction {

    private int numerator;
    private int denominator;

    public Fraction(int numerator, int denominator) {
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
        return (numerator * 100) / denominator;
    }

    @Override
    public String toString() {
        return numerator + "/" + denominator;
    }

    public String toPercent() {
        return getPercent() + "%";
    }

}
