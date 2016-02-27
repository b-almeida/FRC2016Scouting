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

    private SuccessRate lowShootingSuccess;
    private SuccessRate highShootingSuccess;

    private SuccessRate defenseLowBarBreachSuccess;
    private SuccessRate defenseCategoryABreachSuccess;
    private SuccessRate defenseCategoryBBreachSuccess;
    private SuccessRate defenseCategoryCBreachSuccess;
    private SuccessRate defenseCategoryDBreachSuccess;


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

        this.lowShootingSuccess = new SuccessRate();
        this.highShootingSuccess = new SuccessRate();

        this.defenseLowBarBreachSuccess = new SuccessRate();
        this.defenseCategoryABreachSuccess = new SuccessRate();
        this.defenseCategoryBBreachSuccess = new SuccessRate();
        this.defenseCategoryCBreachSuccess = new SuccessRate();
        this.defenseCategoryDBreachSuccess = new SuccessRate();
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

    public SuccessRate getLowShootingSuccess() {
        return lowShootingSuccess;
    }

    public SuccessRate getHighShootingSuccess() {
        return highShootingSuccess;
    }

    public SuccessRate getDefenseLowBarBreachSuccess() {
        return defenseLowBarBreachSuccess;
    }

    public SuccessRate getDefenseCategoryABreachSuccess() {
        return defenseCategoryABreachSuccess;
    }

    public SuccessRate getDefenseCategoryBBreachSuccess() {
        return defenseCategoryBBreachSuccess;
    }

    public SuccessRate getDefenseCategoryCBreachSuccess() {
        return defenseCategoryCBreachSuccess;
    }

    public SuccessRate getDefenseCategoryDBreachSuccess() {
        return defenseCategoryDBreachSuccess;
    }

}
