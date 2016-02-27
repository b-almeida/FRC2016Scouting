package com.example.brunoalmeida.frc2016scouting.data;

/**
 * The data for one robot's match.
 * Includes the robot, its allies, and its statistics.
 *
 * Created by Bruno on 2016-02-25.
 */
public class Match {

    private long id;

    private int teamNumber;
    private int ally1TeamNumber;
    private int ally2TeamNumber;
    private int opponent1TeamNumber;
    private int opponent2TeamNumber;
    private int opponent3TeamNumber;

    private SuccessRate lowShootingSuccessRate;
    private SuccessRate highShootingSuccessRate;

    private SuccessRate defenseLowBarBreachSuccessRate;
    private SuccessRate defenseCategoryABreachSuccessRate;
    private SuccessRate defenseCategoryBBreachSuccessRate;
    private SuccessRate defenseCategoryCBreachSuccessRate;
    private SuccessRate defenseCategoryDBreachSuccessRate;


    public Match(long id,
                 int teamNumber,
                 int ally1TeamNumber,
                 int ally2TeamNumber,
                 int opponent1TeamNumber,
                 int opponent2TeamNumber,
                 int opponent3TeamNumber) {
        this.id = id;
        this.teamNumber = teamNumber;
        this.ally1TeamNumber = ally1TeamNumber;
        this.ally2TeamNumber = ally2TeamNumber;
        this.opponent1TeamNumber = opponent1TeamNumber;
        this.opponent2TeamNumber = opponent2TeamNumber;
        this.opponent3TeamNumber = opponent3TeamNumber;

        this.lowShootingSuccessRate = new SuccessRate();
        this.highShootingSuccessRate = new SuccessRate();

        this.defenseLowBarBreachSuccessRate = new SuccessRate();
        this.defenseCategoryABreachSuccessRate = new SuccessRate();
        this.defenseCategoryBBreachSuccessRate = new SuccessRate();
        this.defenseCategoryCBreachSuccessRate = new SuccessRate();
        this.defenseCategoryDBreachSuccessRate = new SuccessRate();
    }

    public Match(int teamNumber,
                 int ally1TeamNumber,
                 int ally2TeamNumber,
                 int opponent1TeamNumber,
                 int opponent2TeamNumber,
                 int opponent3TeamNumber) {
        this(
                -1,
                teamNumber,
                ally1TeamNumber,
                ally2TeamNumber,
                opponent1TeamNumber,
                opponent2TeamNumber,
                opponent3TeamNumber);
    }

    @Override
    public String toString() {
        String str = "";

        str += "id = " + id;

        str += "\n" + "teamNumber = " + teamNumber;
        str += "\n" + "ally1TeamNumber = " + ally1TeamNumber;
        str += "\n" + "ally2TeamNumber = " + ally2TeamNumber;
        str += "\n" + "opponent1TeamNumber" + opponent1TeamNumber;
        str += "\n" + "opponent2TeamNumber" + opponent2TeamNumber;
        str += "\n" + "opponent3TeamNumber" + opponent3TeamNumber;

        str += "\n" + "lowShootingSuccessRate" + lowShootingSuccessRate;
        str += "\n" + "highShootingSuccessRate" + highShootingSuccessRate;

        str += "\n" + "defenseLowBarBreachSuccessRate" + defenseLowBarBreachSuccessRate;
        str += "\n" + "defenseCategoryABreachSuccessRate" + defenseCategoryABreachSuccessRate;
        str += "\n" + "defenseCategoryBBreachSuccessRate" + defenseCategoryBBreachSuccessRate;
        str += "\n" + "defenseCategoryCBreachSuccessRate" + defenseCategoryCBreachSuccessRate;
        str += "\n" + "defenseCategoryDBreachSuccessRate" + defenseCategoryDBreachSuccessRate;

        return str;
    }

    public long getId() {
        return id;
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

    public SuccessRate getLowShootingSuccessRate() {
        return lowShootingSuccessRate;
    }

    public SuccessRate getHighShootingSuccessRate() {
        return highShootingSuccessRate;
    }

    public SuccessRate getDefenseLowBarBreachSuccessRate() {
        return defenseLowBarBreachSuccessRate;
    }

    public SuccessRate getDefenseCategoryABreachSuccessRate() {
        return defenseCategoryABreachSuccessRate;
    }

    public SuccessRate getDefenseCategoryBBreachSuccessRate() {
        return defenseCategoryBBreachSuccessRate;
    }

    public SuccessRate getDefenseCategoryCBreachSuccessRate() {
        return defenseCategoryCBreachSuccessRate;
    }

    public SuccessRate getDefenseCategoryDBreachSuccessRate() {
        return defenseCategoryDBreachSuccessRate;
    }

}
