package com.example.brunoalmeida.frc2016scouting.data;

import android.util.Log;

import java.util.EnumMap;

/**
 * The data for a match, focusing on one team's statistics.
 * Includes the team, its allies/opponents, and its statistics.
 *
 * Created by Bruno on 2016-02-25.
 */
public class Match {

    public enum Team {
        ALLY_1("Ally 1"),
        ALLY_2("Ally 2"),
        ALLY_3("Ally 3"),
        OPPONENT_1("Opponent 1"),
        OPPONENT_2("Opponent 2"),
        OPPONENT_3("Opponent 3");

        private String displayString;

        Team(final String displayString) {
            this.displayString = displayString;
        }

        public String getDisplayString() {
            return displayString;
        }
    }

    public enum Statistic {
        LOW_BAR_DEFENSE_BREACH      ("LowBarDefenseBreach",     "Low Bar"),
        CATEGORY_A_DEFENSE_BREACH   ("CategoryADefenseBreach",  "Category A"),
        CATEGORY_B_DEFENSE_BREACH   ("CategoryBDefenseBreach",  "Category B"),
        CATEGORY_C_DEFENSE_BREACH   ("CategoryCDefenseBreach",  "Category C"),
        CATEGORY_D_DEFENSE_BREACH   ("CategoryDDefenseBreach",  "Category D"),

        LOW_GOAL_SHOOTING   ("LowGoalShooting",     "Low Shot"),
        HIGH_GOAL_SHOOTING  ("HighGoalShooting",    "High Shot"),

        TOWER_CHALLENGE ("TowerChallenge",  "Challenge Tower"),
        TOWER_SCALE     ("TowerScale",      "Scale Tower");


        private String databaseColumnString;
        private String displayString;


        Statistic(final String databaseColumnString, final String displayString) {
            this.databaseColumnString = databaseColumnString;
            this.displayString = displayString;
        }

        public String getDatabaseColumnString() {
            return databaseColumnString;
        }

        public String getDisplayString() {
            return displayString;
        }
    }




    private long id;
    private String description;
    private EnumMap<Team, Integer> teamNumbers = new EnumMap<>(Team.class);
    private EnumMap<Statistic, SuccessRate> statistics = new EnumMap<>(Statistic.class);




    public Match(long id,
                 String description,
                 int ally1TeamNumber,
                 int ally2TeamNumber,
                 int ally3TeamNumber,
                 int opponent1TeamNumber,
                 int opponent2TeamNumber,
                 int opponent3TeamNumber) {

        this.id = id;
        this.description = description;

        this.teamNumbers.put(Team.ALLY_1, ally1TeamNumber);
        this.teamNumbers.put(Team.ALLY_2, ally2TeamNumber);
        this.teamNumbers.put(Team.ALLY_3, ally3TeamNumber);
        this.teamNumbers.put(Team.OPPONENT_1, opponent1TeamNumber);
        this.teamNumbers.put(Team.OPPONENT_2, opponent2TeamNumber);
        this.teamNumbers.put(Team.OPPONENT_3, opponent3TeamNumber);

        for (Statistic statistic : Statistic.values()) {
            this.statistics.put(statistic, new SuccessRate());
        }
    }

    public Match(String description,
                 int ally1TeamNumber,
                 int ally2TeamNumber,
                 int ally3TeamNumber,
                 int opponent1TeamNumber,
                 int opponent2TeamNumber,
                 int opponent3TeamNumber) {

        this(
                -1,
                description,
                ally1TeamNumber,
                ally2TeamNumber,
                ally3TeamNumber,
                opponent1TeamNumber,
                opponent2TeamNumber,
                opponent3TeamNumber);
    }




    public long getID() {
        return id;
    }


    public String getDescription() {
        return description;
    }


    public int getTeamNumber(Team team) {
        return teamNumbers.get(team);
    }


    public EnumMap<Statistic, SuccessRate> getStatistics() {
        return statistics;
    }

    public SuccessRate getStatistic(Statistic statistic) {
        return statistics.get(statistic);
    }

    public SuccessRate getStatistic(int index) {
        return getStatistic(Statistic.values()[index]);
    }




    public void setStatistic(Statistic statistic, SuccessRate successRate) {
        statistics.put(statistic, successRate);
    }

    public void setStatistic(Statistic statistic, int successes, int attempts) {
        setStatistic(statistic, new SuccessRate(successes, attempts));
    }

    public void setStatistic(int index, SuccessRate successRate) {
        setStatistic(Statistic.values()[index], successRate);
    }

    public void setStatistic(int index, int successes, int attempts) {
        setStatistic(index, new SuccessRate(successes, attempts));
    }




    @Override
    public String toString() {
        String str = "";

        str += "id = " + getID();

        str += ", description = " + getDescription();

        for (Team team : Team.values()) {
            str += ", teamNumber: " + team.toString() + " = " + getTeamNumber(team);
        }

        for (Statistic statistic : Statistic.values()) {
            str += ", statistic: " + statistic.toString() + " = " + getStatistic(statistic);
        }

        return str;
    }

}
