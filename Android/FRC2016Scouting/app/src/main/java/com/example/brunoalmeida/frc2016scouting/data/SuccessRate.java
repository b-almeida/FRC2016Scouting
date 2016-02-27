package com.example.brunoalmeida.frc2016scouting.data;

/**
 * Represents a fraction, with a numerator and denominator.
 * Can be represented as a normal fraction or a percent.
 *
 * Created by Bruno on 2016-02-25.
 */
public class SuccessRate {

    private int successes;
    private int attempts;

    public SuccessRate() {
        this(0, 0);
    }

    public SuccessRate(int successes, int attempts) {
        this.successes = successes;
        this.attempts = attempts;
    }

    public int getSuccesses() {
        return successes;
    }

    public int getAttempts() {
        return attempts;
    }

    public int getPercentRate() {
        if (attempts == 0) {
            return 0;
        } else {
            return (successes * 100) / attempts;
        }
    }

    @Override
    public String toString() {
        return successes + "/" + attempts;
    }

    public String toPercentRate() {
        return getPercentRate() + "%";
    }

}
