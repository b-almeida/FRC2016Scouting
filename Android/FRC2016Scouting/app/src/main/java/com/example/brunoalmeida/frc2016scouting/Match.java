package com.example.brunoalmeida.frc2016scouting;

/**
 * The data for one robot's match.
 * Includes the robot, its allies, and its statistics.
 *
 * Created by Bruno on 2016-02-25.
 */
public class Match {

    private int teamNumber;
    private int ally1TeamNumber;
    private int ally2TeamNumber;
    private int opponent1TeamNumber;
    private int opponent2TeamNumber;
    private int opponent3TeamNumber;

    private Fraction lowShootingSuccess;
    private Fraction highShootingSuccess;

    private Fraction defenseLowBarBreachSuccess;
    private Fraction defenseCategoryABreachSuccess;
    private Fraction defenseCategoryBBreachSuccess;
    private Fraction defenseCategoryCBreachSuccess;
    private Fraction defenseCategoryDBreachSuccess;


    public Match(int teamNumber,
                 int ally1TeamNumber,
                 int ally2TeamNumber,
                 int opponent1TeamNumber,
                 int opponent2TeamNumber,
                 int opponent3TeamNumber) {
        this.teamNumber = teamNumber;
        this.ally1TeamNumber = ally1TeamNumber;
        this.ally2TeamNumber = ally2TeamNumber;
        this.opponent1TeamNumber = opponent1TeamNumber;
        this.opponent2TeamNumber = opponent2TeamNumber;
        this.opponent3TeamNumber = opponent3TeamNumber;

        this.lowShootingSuccess = new Fraction();
        this.highShootingSuccess = new Fraction();

        this.defenseLowBarBreachSuccess = new Fraction();
        this.defenseCategoryABreachSuccess = new Fraction();
        this.defenseCategoryBBreachSuccess = new Fraction();
        this.defenseCategoryCBreachSuccess = new Fraction();
        this.defenseCategoryDBreachSuccess = new Fraction();
    }

    public int getTeamNumber() {
        return teamNumber;
    }

    public int getAlly1TeamNumber() {
        return ally1TeamNumber;
    }

    public int getAlly2TeamNumber() {
        return ally2TeamNumber;
    }

    public int getOpponent1TeamNumber() {
        return opponent1TeamNumber;
    }

    public int getOpponent2TeamNumber() {
        return opponent2TeamNumber;
    }

    public int getOpponent3TeamNumber() {
        return opponent3TeamNumber;
    }

    public Fraction getLowShootingSuccess() {
        return lowShootingSuccess;
    }

    public Fraction getHighShootingSuccess() {
        return highShootingSuccess;
    }

    public Fraction getDefenseLowBarBreachSuccess() {
        return defenseLowBarBreachSuccess;
    }

    public Fraction getDefenseCategoryABreachSuccess() {
        return defenseCategoryABreachSuccess;
    }

    public Fraction getDefenseCategoryBBreachSuccess() {
        return defenseCategoryBBreachSuccess;
    }

    public Fraction getDefenseCategoryCBreachSuccess() {
        return defenseCategoryCBreachSuccess;
    }

    public Fraction getDefenseCategoryDBreachSuccess() {
        return defenseCategoryDBreachSuccess;
    }

}
