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
        ALLY_1 ("Ally 1"),
        ALLY_2 ("Ally 2"),
        ALLY_3 ("Ally 3"),
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

    public enum Shooting {
        LOW_GOAL("Low Goal"),
        HIGH_GOAL("High Goal");

        private String displayString;

        Shooting(final String displayString) {
            this.displayString = displayString;
        }

        public String getDisplayString() {
            return displayString;
        }
    }

    public enum DefenseBreach {
        LOW_BAR("Low Bar"),
        CATEGORY_A("Category A"),
        CATEGORY_B("Category B"),
        CATEGORY_C("Category C"),
        CATEGORY_D("Category D");

        private String displayString;

        DefenseBreach(final String displayString) {
            this.displayString = displayString;
        }

        public String getDisplayString() {
            return displayString;
        }
    }




    private long id;
    private String description;
    private EnumMap<Team, Integer> teamNumbers = new EnumMap<>(Team.class);
    private EnumMap<Shooting, SuccessRate> shootingRates = new EnumMap<>(Shooting.class);
    private EnumMap<DefenseBreach, SuccessRate> defenseBreachRates = new EnumMap<>(DefenseBreach.class);




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

        for (Shooting shooting : Shooting.values()) {
            this.shootingRates.put(shooting, new SuccessRate());
        }

        for (DefenseBreach defenseBreach : DefenseBreach.values()) {
            this.defenseBreachRates.put(defenseBreach, new SuccessRate());
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


    public EnumMap<Shooting, SuccessRate> getShootingRates() {
        return shootingRates;
    }

    public SuccessRate getShootingRate(Shooting shooting) {
        return shootingRates.get(shooting);
    }

    public SuccessRate getShootingRate(int index) {
        return getShootingRate(Shooting.values()[index]);
    }


    public EnumMap<DefenseBreach, SuccessRate> getDefenseBreachRates() {
        return defenseBreachRates;
    }

    public SuccessRate getDefenseBreachRate(DefenseBreach defenseBreach) {
        return defenseBreachRates.get(defenseBreach);
    }

    public SuccessRate getDefenseBreachRate(int index) {
        return getDefenseBreachRate(DefenseBreach.values()[index]);
    }




    public void setShootingRate(Shooting shooting, int successes, int attempts) {
        shootingRates.put(shooting, new SuccessRate(successes, attempts));
    }

    public void setShootingRate(int index, int successes, int attempts) {
        setShootingRate(Shooting.values()[index], successes, attempts);
    }


    public void setDefenseBreachRate(DefenseBreach defenseBreach, int successes, int attempts) {
        defenseBreachRates.put(defenseBreach, new SuccessRate(successes, attempts));
    }

    public void setDefenseBreachRate(int index, int successes, int attempts) {
        setDefenseBreachRate(DefenseBreach.values()[index], successes, attempts);
    }




    @Override
    public String toString() {
        String str = "";

        str += "id = " + getID();

        str += "\n" + "description = " + getDescription();

        for (Team team : Team.values()) {
            str += "\n" + team.toString() + " teamNumber = " +
                    getTeamNumber(team);
        }

        for (Shooting shooting : Shooting.values()) {
            str += "\n" + shooting.toString() + " shootingRate = " +
                    getShootingRate(shooting);
        }

        for (DefenseBreach defenseBreach : DefenseBreach.values()) {
            str += "\n" + defenseBreach.toString() + " defenseBreachRate = " +
                    getDefenseBreachRate(defenseBreach);
        }

        return str;
    }

}
